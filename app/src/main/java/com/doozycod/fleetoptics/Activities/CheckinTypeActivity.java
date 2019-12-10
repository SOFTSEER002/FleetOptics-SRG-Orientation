package com.doozycod.fleetoptics.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.doozycod.fleetoptics.R;

public class CheckinTypeActivity extends AppCompatActivity {
    Button packageDelivery,checkInVisitor,backCheckin;
    private void initUI() {
        checkInVisitor = findViewById(R.id.checkInVisitor);
        packageDelivery = findViewById(R.id.packageDelivery);
        backCheckin = findViewById(R.id.backCheckin);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_checkin_type);
//        hide actionbar
        getSupportActionBar().hide();
//        typecasting
        initUI();

//        onClick events
        onClickListener();

    }

    private void onClickListener() {
//      start Package activity
        packageDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckinTypeActivity.this,PackageDeliveryActivity.class));
            }
        });
//        start Check in visitor
        checkInVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckinTypeActivity.this, PurposeofVisitActivity.class));
            }
        });
//        finish activity
        backCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition( R.anim.slide_in,R.anim.slide_out);

    }
}
