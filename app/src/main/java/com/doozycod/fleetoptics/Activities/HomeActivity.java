package com.doozycod.fleetoptics.Activities;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.doozycod.fleetoptics.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RelativeLayout main_screen;
    LinearLayout SigninSignoutButton;
    TextView text_signin;
    Button signinButton, signoutButton;

    private void initUI() {
        main_screen = findViewById(R.id.main_screen);
        SigninSignoutButton = findViewById(R.id.SigninSignoutButton);
        text_signin = findViewById(R.id.text_signin);
        signinButton = findViewById(R.id.signinBtn);
        signoutButton = findViewById(R.id.signoutButton);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

//        hide Action Bar
        getSupportActionBar().hide();

//      Permission for Camera and Storage
        permissionCheck();
//          typecasting
        initUI();
//        on Click events
        onClickListeners();
    }

    private void onClickListeners() {
//         on click one time for animation
        main_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    text_signin.setVisibility(View.GONE);
                    SigninSignoutButton.setVisibility(View.VISIBLE);

                main_screen.setEnabled(false);

            }
        });
//        start signin Activity
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CheckinTypeActivity.class));
//                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
//        start signout Activity
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SignOutActivity.class));
//                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

//    permission for runtime using dexter!
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void permissionCheck() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ANSWER_PHONE_CALLS,
                        Manifest.permission.USE_SIP,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_CALL_LOG,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
            }

        }).check();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition( R.anim.slide_in,R.anim.slide_out);

    }
}
