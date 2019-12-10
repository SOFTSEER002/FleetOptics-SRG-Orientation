package com.doozycod.fleetoptics.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.doozycod.fleetoptics.R;

public class InterviewActivity extends AppCompatActivity {
    CheckBox checkBox;
    Button submitButton, interBackBtn;
    EditText visitorName, visitorEmail, visitorPhoneNo;
    String checkinType;


    //    typecasting method
    private void initUI() {
        checkBox = findViewById(R.id.checkBox);
        submitButton = findViewById(R.id.submitInterButton);
        interBackBtn = findViewById(R.id.interBackBtn);
        visitorName = findViewById(R.id.visitorName);
        visitorEmail = findViewById(R.id.visitorEmail);
        visitorPhoneNo = findViewById(R.id.visitorPhoneNo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        set Activity FULL SCREEN VIEW/ hide status
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_interview);

//        hide action bar
        getSupportActionBar().hide();
//        type casting
        initUI();
        checkinType = getIntent().getStringExtra("checkinType");
        submitButton.setEnabled(false);
        onClickListener();
    }

    private void onClickListener() {

//        checkbox set submit button enable/disable
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    submitButton.setEnabled(true);
                } else {
                    submitButton.setEnabled(false);
                }
            }
        });
//        finish activity
        interBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//        submit form and goto take picture/camera Activity
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                validation for visitor details
                if (visitorName.getText().toString().equals("")) {
                    visitorName.setError("Please Enter Name");
                    return;
                }
                if (visitorEmail.getText().toString().equals("")) {
                    visitorEmail.setError("Please Enter Email");
                    return;
                }
                if (visitorPhoneNo.getText().toString().equals("")) {
                    visitorEmail.setError("Please Enter Phone no");
                    return;
                }

//                start camera activity
                else {
                    Intent intent = new Intent(InterviewActivity.this, CameraActivity.class);
                    intent.putExtra("InterviewActivity", "InterviewActivity");
                    intent.putExtra("CheckinType", "Visit Employee/Appointment");
                    intent.putExtra("purpose_of_visit", "Interview");
                    intent.putExtra("fullname", visitorName.getText().toString());
                    intent.putExtra("email", visitorEmail.getText().toString());
                    intent.putExtra("phone", visitorPhoneNo.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

}
