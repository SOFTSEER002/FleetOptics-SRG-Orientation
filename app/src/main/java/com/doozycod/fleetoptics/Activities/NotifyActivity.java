package com.doozycod.fleetoptics.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.doozycod.fleetoptics.Model.AppointmentResultModel;
import com.doozycod.fleetoptics.R;
import com.doozycod.fleetoptics.Service.ApiService;
import com.doozycod.fleetoptics.Service.ApiUtils;
import com.doozycod.fleetoptics.Utils.CustomProgressBar;
import com.doozycod.fleetoptics.Utils.SharedPreferencesMethod;
import com.mizuvoip.jvoip.SipStack;
import com.skyfishjy.library.RippleBackground;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifyActivity extends AppCompatActivity {
    TextView message, message2;
    ApiService apiService;
    CustomProgressBar customProgressBar;
    public static String LOGTAG = "FleetOptics";
    SipStack mysipclient = null;
    boolean terminateNotifThread = false;
    GetNotificationsThread notifThread = null;
    public static NotifyActivity instance = null;
    String currentDateandTime;
    Runnable myRunnable;
    boolean isRunning = false;
    boolean isSpeaked = false;
    RippleBackground rippleBackground;
    String empContactNumber = "";
    SharedPreferencesMethod sharedPreferencesMethod;

    //    typecasting method
    private void initUI() {
        message = findViewById(R.id.message);
        message2 = findViewById(R.id.message2);
        rippleBackground = findViewById(R.id.content);
//        ImageView imageView = (ImageView) findViewById(R.id.centerImage);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notify);

//        hide actionbar
        getSupportActionBar().hide();

        sharedPreferencesMethod = new SharedPreferencesMethod(this);
//         typecasting
        initUI();

//      activity instance
        instance = this;
//          progress bar
        customProgressBar = new CustomProgressBar(this);
//        api service
        apiService = ApiUtils.getAPIService();
//        get Date & Time
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        currentDateandTime = sdf.format(new Date());

//      default message
        message.setText("Please Wait...");


        myRunnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(NotifyActivity.this, SplashActivity.class));
                finish();

            }
        };
//        if Package not require Signature
        if (getIntent().hasExtra("no_sign")) {
            message.setText("Please leave the package here, Thank You!");
            message2.setText("");
            rippleBackground.setVisibility(View.GONE);
            new Handler().postDelayed(myRunnable, 10000);
//            return;
        }
        if (getIntent().hasExtra("Specific")) {
//
            empContactNumber = getIntent().getStringExtra("empPhoneNo");
            //            start Voip call
            voipCallStart();
        }
        if (getIntent().hasExtra("signature")) {
            empContactNumber = getIntent().getStringExtra("empPhoneNo");
//            start Voip call
            voipCallStart();
        }
        if (getIntent().hasExtra("appointment1")) {
            String encodedImage = Base64.encodeToString(convert(), Base64.DEFAULT);
            Log.e("encodedImage", "onCreate: " + encodedImage);
//            message.setText("Please Wait...");
            appointmentAPI(getIntent().getStringExtra("checkin"), getIntent().getStringExtra("purpose"), getIntent().getStringExtra("name"), getIntent().getStringExtra("co_name"),
                    getIntent().getStringExtra("emailID"), getIntent().getStringExtra("phone_no"), currentDateandTime, encodedImage, getIntent().getStringExtra("empId"));

        }
        if (getIntent().hasExtra("interview")) {
            String encodedImage = Base64.encodeToString(convert(), Base64.DEFAULT);
//            message.setText("Please Wait…");

            appointmentAPI(getIntent().getStringExtra("checkin"), getIntent().getStringExtra("purpose"), getIntent().getStringExtra("name"), "",
                    getIntent().getStringExtra("emailID"), getIntent().getStringExtra("phone_no"), currentDateandTime, encodedImage, "2");
//            return;
        }
    }

    //    convert image to bitmap,bitmap to bytes
    private byte[] convert() {
        Bitmap bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/FleetOptics/" + "visitor.png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        return baos.toByteArray();
    }

    //    logs for voip call
    @SuppressLint("SimpleDateFormat")
    public void DisplayLogs(String logmsg) {
        if (logmsg == null || logmsg.length() < 1) return;

        if (logmsg.length() > 2500) logmsg = logmsg.substring(0, 300) + "...";
        logmsg = "[" + new java.text.SimpleDateFormat("HH:mm:ss:SSS").format(Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime()) + "] " + logmsg + "\r\n";

        Log.e(LOGTAG, logmsg);
    }

    //    notification Thread
    public class GetNotificationsThread extends Thread {
        String sipnotifications = "";

        public void run() {
            try {
                try {
                    Thread.currentThread().setPriority(4);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                //we are lowering this thread priority a bit to give more chance for our main GUI thread

                while (!terminateNotifThread) {
                    try {
                        sipnotifications = "";
                        if (mysipclient != null) {
                            //get notifications from the SIP stack
                            sipnotifications = mysipclient.GetNotificationsSync();

                            if (sipnotifications != null && sipnotifications.length() > 0) {
                                // send notifications to Main thread using a Handler
                                Message messageToMainThread = new Message();
                                Bundle messageData = new Bundle();
                                messageToMainThread.what = 0;
                                messageData.putString("notifmessages", sipnotifications);
                                messageToMainThread.setData(messageData);
                                NotifThreadHandler.sendMessage(messageToMainThread);
                            }
                        }

                        if ((sipnotifications == null || sipnotifications.length() < 1) && !terminateNotifThread) {
                            //some error occured. sleep a bit just to be sure to avoid busy loop
                            GetNotificationsThread.sleep(1);
                        }
                        continue;
                    } catch (Throwable e) {
                        Log.e(LOGTAG, "ERROR, WorkerThread on run()intern", e);
                    }
                    if (!terminateNotifThread) {
                        GetNotificationsThread.sleep(10);
                    }
                }
            } catch (Throwable e) {
                Log.e(LOGTAG, "WorkerThread on run()");
            }
        }
    }

    //get the notifications from the GetNotificationsThread thread
    @SuppressLint("HandlerLeak")
    public static Handler NotifThreadHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            try {
                if (msg == null || msg.getData() == null) return;
                ;
                Bundle resBundle = msg.getData();

                String receivedNotif = msg.getData().getString("notifmessages");

                if (receivedNotif != null && receivedNotif.length() > 0)
                    instance.ReceiveNotifications(receivedNotif);

            } catch (Throwable e) {
                Log.e(LOGTAG, "NotifThreadHandler handle Message" + e);
            }
        }
    };
    //process notificatins phrase 1: split by line (we can receive multiple notifications separated by \r\n)
    String[] notarray = null;

    //    receiving status of call
    public void ReceiveNotifications(String notifs) {
        if (notifs == null || notifs.length() < 1) return;
        notarray = notifs.split("\r\n");

        if (notarray == null || notarray.length < 1) return;

        for (String s : notarray) {
            if (s != null && s.length() > 0) {
                ProcessNotifications(s);
            }
        }
        /*for (int i = 0; i < notarray.length; i++) {
            if (notarray[i] != null && notarray[i].length() > 0) {
                ProcessNotifications(notarray[i]);
            }
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DisplayLogs("ondestroy");
        terminateNotifThread = true;
        if (mysipclient != null) {
            DisplayLogs("Stop SipStack");
            mysipclient.Stop(true);
        }
        mysipclient = null;
        notifThread = null;
    }

    //    VOIP call method
    void voipCallStart() {
        Log.e(LOGTAG, "voipCallStart: empContactNumber " + empContactNumber);
        DisplayLogs("Start on click");
        try {
            // start SipStack if it's not already running
            if (mysipclient == null) {
                DisplayLogs("Start SipStack");

                //initialize the SIP engine
                mysipclient = new SipStack();
                mysipclient.Init(this);
                mysipclient.SetParameter("loglevel", sharedPreferencesMethod.getLogLevel()); //set loglevel
                mysipclient.SetParameter("serveraddress", sharedPreferencesMethod.getServerAddress()); //set your voip server domain or IP:port
                mysipclient.SetParameter("username", sharedPreferencesMethod.getUserName()); //set SIP username
                mysipclient.SetParameter("password", sharedPreferencesMethod.getPassword()); //set SIP password

                //start my event listener thread
                notifThread = new GetNotificationsThread();
                notifThread.start();
//                customProgressBar.showProgress();

                //start the SIP engine
                mysipclient.Start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mysipclient.Call(-1, empContactNumber);
                        mysipclient.SetSpeakerMode(!mysipclient.IsLoudspeaker());
                    }
                }, 1500);

                //mysipclient.Register();

            } else {
                DisplayLogs("SipStack already started");
            }
        } catch (Exception e) {
            DisplayLogs("ERROR, StartSipStack");
        }
    }

    //process notificatins phrase 2: processing notification strings
    public void ProcessNotifications(String line) {
        DisplayStatus(line); //we just display them in this simple test application
        //see the Notifications section in the documentation about the possible messages (parse the line string and process them after your needs)
    }

    //    check Call connection/state
    @SuppressLint("SetTextI18n")
    public void DisplayStatus(String stat) {
        if (stat == null) return;

        DisplayLogs("Status: 11" + stat);

        rippleBackground.startRippleAnimation();
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {

//            }
//        });
        //        if call state is CALLING / RINGING
        if (stat.contains("Calling") || stat.contains("Ringing")) {
            Log.e(LOGTAG, "DisplayStatus: Ringing...");

            if (getIntent().hasExtra("Specific")) {
//
                message.setText("The recipient has been notified… Please wait.");
//                message2.setText("Someone will be with you shortly to receive the package… Thank You!");
            }
            if (getIntent().hasExtra("signature")) {
                message.setText("Please wait while we retrieve someone for you.");

            }
            if (getIntent().hasExtra("appointment1")) {
                String encodedImage = Base64.encodeToString(convert(), Base64.DEFAULT);
                Log.e("encodedImage", "onCreate: " + encodedImage);
                message.setText("Please Wait...");

//                appointmentAPI(getIntent().getStringExtra("checkin"), getIntent().getStringExtra("purpose"), getIntent().getStringExtra("name"), getIntent().getStringExtra("co_name"),
//                        getIntent().getStringExtra("emailID"), getIntent().getStringExtra("phone_no"), currentDateandTime, encodedImage, getIntent().getStringExtra("empId"));

            }
            if (getIntent().hasExtra("interview")) {
                String encodedImage = Base64.encodeToString(convert(), Base64.DEFAULT);
                message.setText("Please Wait…");

//                appointmentAPI(getIntent().getStringExtra("checkin"), getIntent().getStringExtra("purpose"), getIntent().getStringExtra("name"), "",
//                        getIntent().getStringExtra("emailID"), getIntent().getStringExtra("phone_no"), currentDateandTime, encodedImage, "2");
//            return;
            }
        }

//        if call state is SPEAKING/ IN PROGRESS
        if (stat.contains("Speaking (")) {
            rippleBackground.stopRippleAnimation();
            rippleBackground.setVisibility(View.GONE);

            Log.e(LOGTAG, "DisplayStatus: Speaking");
            if (getIntent().hasExtra("Specific")) {

                message.setText("Someone will be with you shortly to receive the package… Thank You!");

            }
            if (getIntent().hasExtra("signature")) {
//                message.setText("Please wait while we retrieve someone for you.");
                message.setText("Someone will be with you shortly to receive the package… Thank You!");

            }
            if (getIntent().hasExtra("appointment1")) {
                String encodedImage = Base64.encodeToString(convert(), Base64.DEFAULT);
                Log.e("encodedImage", "onCreate: " + encodedImage);
                message.setText(getIntent().getStringExtra("empName") + " has been notified and will be with you shortly… Thank You!");
            }

            if (getIntent().hasExtra("interview")) {
                String encodedImage = Base64.encodeToString(convert(), Base64.DEFAULT);
                message.setText("Someone will be with you shortly… Thank You!");
            }
            isSpeaked = true;

        }
//        if call state is REJECTED OR NOT PICKED UP
        if (stat.contains("call rejected: Busy Here") || stat.contains("503 server failure Service Unavailable") || stat.contains("Service Unavailable")) {
            if (rippleBackground.isRippleAnimationRunning()) {
                rippleBackground.stopRippleAnimation();
                rippleBackground.setVisibility(View.GONE);
            }

            message.setTextColor(Color.RED);
            if (getIntent().hasExtra("Specific")) {
                message.setText("Sorry. The recipient is not available at this time, Please try again later.");
            }
            if (getIntent().hasExtra("signature")) {
//                message.setText("Please wait while we retrieve someone for you.");
                message.setText("Sorry. No one is available at this time, Please try again later.");
            }
            if (getIntent().hasExtra("appointment1")) {
                String encodedImage = Base64.encodeToString(convert(), Base64.DEFAULT);
                Log.e("encodedImage", "onCreate: " + encodedImage);
                message.setText("Sorry." + getIntent().getStringExtra("empName") + " is not available at this time.\n Please try again later." +
                        "\nThank you for stopping by!");
            }
            if (getIntent().hasExtra("interview")) {
                message.setText("Sorry. No one is available for your interview at this time.\n Your details have been submitted – HR will contact you as soon as possible.");
            }
//                go back to home screen
            delayToHome();
        }
//        if call state is DISCONNECT
        if (stat.contains("Call duration: 0 sec") || stat.contains("Finished,") || stat.contains("Call Finished")) {
            if (rippleBackground.isRippleAnimationRunning()) {
                rippleBackground.stopRippleAnimation();
                rippleBackground.setVisibility(View.GONE);
            }

            message.setTextColor(Color.RED);
            if (isSpeaked) {
                Log.e(LOGTAG, "DisplayStatus: Already Speaked");
            } else {
                if (getIntent().hasExtra("Specific")) {
                    message.setText("Sorry. The recipient is not available at this time, Please try again later.");
                }
                if (getIntent().hasExtra("signature")) {
//                message.setText("Please wait while we retrieve someone for you.");
                    message.setText("Sorry. No one is available at this time, Please try again later.");
                }
                if (getIntent().hasExtra("appointment1")) {
                    String encodedImage = Base64.encodeToString(convert(), Base64.DEFAULT);
                    Log.e("encodedImage", "onCreate: " + encodedImage);
                    message.setText("Sorry." + getIntent().getStringExtra("empName") + " is not available at this time.\n Please try again later." +
                            "\nThank you for stopping by!");
                }
                if (getIntent().hasExtra("interview")) {
                    message.setText("Sorry. No one is available for your interview at this time.\n Your details have been submitted – HR will contact you as soon as possible.");
                }
            }
//                go back to home screen
            delayToHome();
        }
    }

    //    10 sec delay to to go back home activity
    void delayToHome() {
        if (!isRunning) {
            isRunning = true;
//        handler to return to home screen after ten seconds
            new Handler().postDelayed(myRunnable, 10000);
        }
    }

    @Override
    public void onBackPressed() {

       /* startActivity(new Intent(NotifyActivity.this, HomeActivity.class));
        finishAffinity();*/
    }

    //    appointment api for visitor
    void appointmentAPI(String checkinType, String purpose_of_visit, String fullname, String company_name,
                        String email_address, String phone_no, String timestamp, String image, String empId) {
//        Log.e("appointmentAPI", "Array: " + fullname + empId);
        apiService.appointment(checkinType, purpose_of_visit, fullname, company_name, email_address, phone_no, timestamp, image, empId).enqueue(new Callback<AppointmentResultModel>() {
            @Override
            public void onResponse(Call<AppointmentResultModel> call, Response<AppointmentResultModel> response) {
                if (response.isSuccessful()) {
                    Log.e("Response", "onResponse: " + response.body().getMessage());
                    if (response.body().getEmployee_contact() != null) {
                        empContactNumber = response.body().getEmployee_contact();
                    } else {
//                        HR Contact Number
                        empContactNumber = "7001";
                    }
                    //            start Voip call
                    voipCallStart();
                }
            }

            @Override
            public void onFailure(Call<AppointmentResultModel> call, Throwable t) {
//                customProgressBar.hideProgress();
                Toast.makeText(NotifyActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();
                Log.e("Response", "onResponse: " + t.getMessage());
            }
        });
    }

}
