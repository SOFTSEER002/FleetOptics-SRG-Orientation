package com.doozycod.fleetoptics.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.fleetoptics.Adapter.SignOutRecyclerAdapter;
import com.doozycod.fleetoptics.Interface.CallbackListener;
import com.doozycod.fleetoptics.Model.GetCurrentVisitors;
import com.doozycod.fleetoptics.Model.ResultModel;
import com.doozycod.fleetoptics.R;
import com.doozycod.fleetoptics.Service.ApiService;
import com.doozycod.fleetoptics.Service.ApiUtils;
import com.doozycod.fleetoptics.Utils.CustomProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignOutActivity extends AppCompatActivity implements CallbackListener {
    private final String TAG = "SignOutActivity";
    RecyclerView recyclerView;
    SignOutRecyclerAdapter signOutRecyclerAdapter;
    Button SignOutSubmitButton, signOutBackBtn;
    ApiService apiService;
    List<GetCurrentVisitors.visitors> getCurrentVisitorsList = new ArrayList<>();
    EditText searchVisitor;
    String visitorEmail = "";
    CustomProgressBar customProgressBar;
    private ProgressDialog progressDialog;

    //    typecasting method
    private void initUI() {
        recyclerView = findViewById(R.id.signoutRecyclerView);
        SignOutSubmitButton = findViewById(R.id.SignOutSubmitButton);
        signOutBackBtn = findViewById(R.id.signOutBackBtn);
        searchVisitor = findViewById(R.id.signOutDirectory);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_sign_out);
        getSupportActionBar().hide();

//         api service start
        apiService = ApiUtils.getAPIService();


        customProgressBar = new CustomProgressBar(this);
//        Typecasting
        initUI();

//        recyclerview properties
        recyclerviewAdapter();

//        on Click Listener
        OnClickListener();

//        getCurrentVisitors for signout
        getCurrentVisitors();
    }

    //    rv properties method
    private void recyclerviewAdapter() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        recyclerView.setAdapter(signOutRecyclerAdapter);
    }


    private void OnClickListener() {
//        finish Activity
        signOutBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchVisitor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setAdapter(signOutRecyclerAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
//        start Home ACtivity on sign out submit button
        SignOutSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                String currentDateandTime = sdf.format(new Date());
                if (!visitorEmail.equals("")) {
                    signOutVisitor(visitorEmail, currentDateandTime);
                }

            }
        });
    }

//    signout visitor search by name
    void filter(String text) {
        List<GetCurrentVisitors.visitors> temp = new ArrayList();
        for (GetCurrentVisitors.visitors d : getCurrentVisitorsList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getFull_name().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        if (!searchVisitor.getText().toString().equals("")) {
            //update recyclerview
            signOutRecyclerAdapter.updateList(temp);
        }
    }

//    signout Api
    void signOutVisitor(String visitorEmail, String timestamp) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            customProgressBar.showProgress();
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }
        apiService.signOutVisitor(visitorEmail, timestamp).enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                if (response.isSuccessful()) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        customProgressBar.hideProgress();
                    } else {
                        progressDialog.dismiss();
                    }
                    if (response.body().getType().equals("success")) {
                        Toast.makeText(SignOutActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignOutActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(SignOutActivity.this, SplashActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    customProgressBar.hideProgress();
                } else {
                    progressDialog.dismiss();
                }
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(SignOutActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    get all Current visitors
    void getCurrentVisitors() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            customProgressBar.showProgress();
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }
        apiService.getCurrentVisitors().enqueue(new Callback<GetCurrentVisitors>() {
            @Override
            public void onResponse(Call<GetCurrentVisitors> call, Response<GetCurrentVisitors> response) {
                if (response.isSuccessful()) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        customProgressBar.hideProgress();
                    } else {
                        progressDialog.dismiss();
                    }
                    getCurrentVisitorsList = response.body().getVisitors();
                    Log.e(TAG, "onResponse: " + getCurrentVisitorsList.get(0).getCheckin_type());
                    signOutRecyclerAdapter = new SignOutRecyclerAdapter(SignOutActivity.this, SignOutActivity.this, getCurrentVisitorsList);
                }
            }

            @Override
            public void onFailure(Call<GetCurrentVisitors> call, Throwable t) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    customProgressBar.hideProgress();
                } else {
                    progressDialog.dismiss();
                }
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(SignOutActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//   get empName and empId
    @Override
    public void onResultListener(String RecipientName, String type) {
        visitorEmail = type;
        if (!type.isEmpty()) {
            SignOutSubmitButton.setEnabled(true);
        }
        searchVisitor.setText(RecipientName);
        Log.e(TAG, "onResultListener: " + type + "   " + RecipientName);
    }
}
