package com.doozycod.fleetoptics.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.doozycod.fleetoptics.Model.ResultModel;
import com.doozycod.fleetoptics.R;
import com.doozycod.fleetoptics.Service.ApiService;
import com.doozycod.fleetoptics.Service.ApiUtils;
import com.doozycod.fleetoptics.Utils.CustomProgressBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageDeliveryActivity extends AppCompatActivity {
    RadioButton yesRadioBtn, noRadioBtn;
    RadioGroup radioGroup;
    Button backPackageDelivery, submitPackageButton;
    ApiService apiService;
    CustomProgressBar customProgressBar;

    private void initUI() {
        yesRadioBtn = findViewById(R.id.yes_radio);
        noRadioBtn = findViewById(R.id.no_radio);
        radioGroup = findViewById(R.id.radioGroup);
        backPackageDelivery = findViewById(R.id.backPackageDelivery);
        submitPackageButton = findViewById(R.id.submitButtonPackage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_package_delivery);
        getSupportActionBar().hide();

//        progressbar
        customProgressBar = new CustomProgressBar(this);

//        apiservice
        apiService = ApiUtils.getAPIService();
//      typecasting
        initUI();

//        on click events
        onClickListener();
    }

    private void onClickListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });
        submitPackageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (yesRadioBtn.isChecked()) {
                    startActivity(new Intent(PackageDeliveryActivity.this, SignatureActivity.class));
                }
                if (noRadioBtn.isChecked()) {
                    getPackageDelivered("Package Delivery", "2", "No", "No");
                }
            }
        });
        backPackageDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    //    Package Delivery Api
    void getPackageDelivered(String CheckinType, String deliverToWhom, String isSignReq, String isSpecificPerson) {
        customProgressBar.showProgress();
        apiService.packageDelivery(CheckinType, deliverToWhom, isSignReq, isSpecificPerson).enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                customProgressBar.hideProgress();

                if (response.isSuccessful()) {
                    if (response.body().getType().equals("success")) {
                        Toast.makeText(PackageDeliveryActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PackageDeliveryActivity.this, NotifyActivity.class);
                        intent.putExtra("no_sign", "no_sign");
                        startActivity(intent);
                    } else {
                        Toast.makeText(PackageDeliveryActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                Toast.makeText(PackageDeliveryActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                customProgressBar.hideProgress();

            }
        });
    }

}
