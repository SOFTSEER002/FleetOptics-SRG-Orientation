package com.doozycod.fleetoptics.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
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

public class SignatureActivity extends AppCompatActivity {
    RadioButton yesRadioBtn, noRadioBtn;
    RadioGroup radioGroup;
    Button backSignature,submitSignatureButton;
    CustomProgressBar customProgressBar;
    ApiService apiService;
    private ProgressDialog progressDialog;

    //    typecasting method
    private void initUI() {
        yesRadioBtn = findViewById(R.id.yes_sign);
        noRadioBtn = findViewById(R.id.no_sign);
        radioGroup = findViewById(R.id.signRadioGroup);
        backSignature = findViewById(R.id.backSignature);
        submitSignatureButton = findViewById(R.id.submitButtonSign);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        set Activity full Screen / hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signature);
//        hide action bar
        getSupportActionBar().hide();

//        progress bar
        customProgressBar = new CustomProgressBar(this);

//        api service
        apiService = ApiUtils.getAPIService();
//        typecasting
        initUI();
//        set Click Listeners
        onClickListener();
    }


    private void onClickListener() {
//          set radio button on check change listener
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });
//        finish Activity on back press
        submitSignatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (yesRadioBtn.isChecked()) {
                    startActivity(new Intent(SignatureActivity.this, SpecificRecipientActivity.class));
                }
                if (noRadioBtn.isChecked()) {
                    getPackageDelivered("Package Delivery", "2", "Yes", "No");
                }
            }
        });
        backSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    //    Package Delivery Api
    void getPackageDelivered(String CheckinType, String deliverToWhom, String isSignReq, String isSpecificPerson) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            customProgressBar.showProgress();
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }
        apiService.packageDelivery(CheckinType, deliverToWhom, isSignReq, isSpecificPerson).enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    customProgressBar.hideProgress();
                } else {
                    progressDialog.dismiss();
                }

                if (response.isSuccessful()) {
                    if (response.body().getType().equals("success")) {
                        Toast.makeText(SignatureActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignatureActivity.this, NotifyActivity.class);
                        intent.putExtra("signature", "signature");
                        intent.putExtra("empPhoneNo", response.body().getEmployee_contact());
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignatureActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                Toast.makeText(SignatureActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    customProgressBar.hideProgress();
                } else {
                    progressDialog.dismiss();
                }

            }
        });
    }
}
