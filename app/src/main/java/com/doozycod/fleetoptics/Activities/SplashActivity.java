package com.doozycod.fleetoptics.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.doozycod.fleetoptics.Model.ServerConfigModel;
import com.doozycod.fleetoptics.R;
import com.doozycod.fleetoptics.Service.ApiService;
import com.doozycod.fleetoptics.Service.ApiUtils;
import com.doozycod.fleetoptics.Utils.SharedPreferencesMethod;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {
    // Animation
    Animation animFadein;
    Animation animFadeout;
    RelativeLayout mainLayout;
    TextView text_signin;
    SharedPreferencesMethod sharedPreferencesMethod;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        set activity as FULL SCREEn/ hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

//        hide Actionbar
        getSupportActionBar().hide();

//        local db for store server Details
        sharedPreferencesMethod = new SharedPreferencesMethod(this);
//        typecasting
        mainLayout = findViewById(R.id.splash);
        text_signin = findViewById(R.id.text_signin);
        mainLayout.setEnabled(false);
        apiService = ApiUtils.getAPIService();
        //        Animation
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        animFadeout = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);
        animFadeout.setAnimationListener(this);
        animFadein.setAnimationListener(this);

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_signin.clearAnimation();
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
//        get Server Details
        getServerConfiguration();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    void getServerConfiguration() {
        apiService.getServerConfig().enqueue(new Callback<ServerConfigModel>() {
            @Override
            public void onResponse(Call<ServerConfigModel> call, Response<ServerConfigModel> response) {

                if (response.isSuccessful()) {
                    Log.e("COLF", "onResponse: " + response.body().getServer_config().getUsername() + "\n" +
                            response.body().getServer_config().getServer_address() + "\n" +
                            response.body().getServer_config().getPassword() + "\n" + response.body().getServer_config().getLog_level());
                    mainLayout.setEnabled(true);
                    sharedPreferencesMethod.spInsert(response.body().getServer_config().getUsername(), response.body().getServer_config().getServer_address(),
                            response.body().getServer_config().getPassword(), response.body().getServer_config().getLog_level());
                }
                //        start Home Activity after 3 seconds of delay
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        text_signin.startAnimation(animFadeout);
                    }
                }, 1000);
            }

            @Override
            public void onFailure(Call<ServerConfigModel> call, Throwable t) {
                Log.e("COLF", "onResponse: " + t.getMessage());
                Toast.makeText(SplashActivity.this, "" + t.getMessage() + "\nRetrying...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SplashActivity.this, SplashActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

}
