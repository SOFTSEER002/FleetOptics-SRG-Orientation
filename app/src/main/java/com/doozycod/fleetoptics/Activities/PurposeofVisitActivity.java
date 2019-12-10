package com.doozycod.fleetoptics.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.fleetoptics.Adapter.RecyclerAdapter;
import com.doozycod.fleetoptics.Interface.CallbackListener;
import com.doozycod.fleetoptics.Model.GetEmployeeModel;
import com.doozycod.fleetoptics.R;
import com.doozycod.fleetoptics.Service.ApiService;
import com.doozycod.fleetoptics.Service.ApiUtils;
import com.doozycod.fleetoptics.Utils.CustomProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurposeofVisitActivity extends AppCompatActivity implements CallbackListener {
    RadioGroup radioGroup;
    RadioButton meetingRadioBtn, interviewRadioBtn, personalRadioBtn;
    EditText search_emp;
    Button sumbitEmpBtn, backBtn;
    RecyclerView recyclerView;
    String purpose = "";
    RecyclerAdapter recyclerAdapter;
    ApiService apiService;
    List<GetEmployeeModel.employees> getEmployeeModels = new ArrayList<>();
    String employeeName = "", empId = "";
    CustomProgressBar customProgressBar;

    private void initUI() {
        search_emp = findViewById(R.id.editTextsearchbar);
        sumbitEmpBtn = findViewById(R.id.sumbitEmpBtn);
        recyclerView = findViewById(R.id.visitor_listview);
        radioGroup = findViewById(R.id.visit_radio_group);
        meetingRadioBtn = findViewById(R.id.meeting);
        interviewRadioBtn = findViewById(R.id.interview);
        personalRadioBtn = findViewById(R.id.personal);
        backBtn = findViewById(R.id.visitCheckinBackBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_check_in_visit);
        getSupportActionBar().hide();
        initUI();
        onClickListener();
//        api service
        apiService = ApiUtils.getAPIService();


        sumbitEmpBtn.setEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        progressbar
        customProgressBar = new CustomProgressBar(this);
//          get Employee data
        getEmployees();
    }

    private void onClickListener() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (meetingRadioBtn.isChecked()) {
                    purpose = "Meeting";
                    if (!search_emp.isEnabled()) {
                        search_emp.setText("");
                    }
                    sumbitEmpBtn.setEnabled(false);
                    search_emp.setEnabled(true);
                    recyclerView.setEnabled(true);
                    meetingRadioBtn.setChecked(true);
                    interviewRadioBtn.setChecked(false);
                    personalRadioBtn.setChecked(false);

                }
                if (interviewRadioBtn.isChecked()) {

                    purpose = "Drop-in Interview";
                    search_emp.setText("");

                    sumbitEmpBtn.setEnabled(true);

                    search_emp.setEnabled(false);
                    recyclerView.setEnabled(false);
                    meetingRadioBtn.setChecked(false);
                    interviewRadioBtn.setChecked(true);
                    personalRadioBtn.setChecked(false);
                }
                if (personalRadioBtn.isChecked()) {
                    purpose = "Personal";
                    search_emp.setEnabled(true);
                    if (!search_emp.isEnabled()) {
                        search_emp.setText("");
                    }
                    sumbitEmpBtn.setEnabled(false);

                    recyclerView.setEnabled(true);
                    meetingRadioBtn.setChecked(false);
                    interviewRadioBtn.setChecked(false);
                    personalRadioBtn.setChecked(true);

                }
            }
        });

//        search perform on text change update list
        search_emp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sumbitEmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if meeting is checked start EmpPersocalCheckin Activity
                if (meetingRadioBtn.isChecked()) {
                    Intent intent = new Intent(PurposeofVisitActivity.this, PersonalMeetingActivity.class);
                    intent.putExtra("purpose", "Meeting");
                    intent.putExtra("empId", empId);
                    intent.putExtra("empName", employeeName);
                    startActivity(intent);
                    return;
                }
//                if interview is checked start Interview Activity
                if (interviewRadioBtn.isChecked()) {

                    Intent intent = new Intent(PurposeofVisitActivity.this, InterviewActivity.class);
                    intent.putExtra("purpose", "Interview");
                    startActivity(intent);
                    return;
                }
//                if personal is checked start EmpPersocalCheckin Activity
                if (personalRadioBtn.isChecked()) {
                    Intent intent = new Intent(PurposeofVisitActivity.this, PersonalMeetingActivity.class);
                    intent.putExtra("purpose", "Personal");
                    intent.putExtra("empId", empId);
                    intent.putExtra("empName", employeeName);
                    startActivity(intent);
                } else {
                    Toast.makeText(PurposeofVisitActivity.this, "Select Visit Type", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    get employee from api
    void filter(String text) {
        List<GetEmployeeModel.employees> temp = new ArrayList();
        for (GetEmployeeModel.employees d : getEmployeeModels) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getFull_name().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        recyclerAdapter.updateList(temp);
    }


//    get All Employees api
    void getEmployees() {
        customProgressBar.showProgress();
        apiService.getAllEmployees().enqueue(new Callback<GetEmployeeModel>() {
            @Override
            public void onResponse(Call<GetEmployeeModel> call, Response<GetEmployeeModel> response) {
                if (response.isSuccessful()) {
                    customProgressBar.hideProgress();
                    getEmployeeModels = response.body().getEmployees();
                    for (int i = 0; i < getEmployeeModels.size(); i++) {
                        Log.e("Employee DATA", "onResponse: " + getEmployeeModels.get(i).getId());
                        recyclerAdapter = new RecyclerAdapter(PurposeofVisitActivity.this, getEmployeeModels, PurposeofVisitActivity.this);
//                        recyclerView.setAdapter(recyclerAdapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<GetEmployeeModel> call, Throwable t) {
                Log.e("Employee DATA", "onResponse: " + t.getMessage());
                customProgressBar.hideProgress();
                Toast.makeText(PurposeofVisitActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    interface to get selected employee name and empID
    @Override
    public void onResultListener(String RecipientName, String id) {
        employeeName = RecipientName;
        empId = id;
        Log.e("NAME", "onResultListener: " + employeeName + "  " + empId);
        sumbitEmpBtn.setEnabled(true);
        search_emp.setText(employeeName);

    }
}
