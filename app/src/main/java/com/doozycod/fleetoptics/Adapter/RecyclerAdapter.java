package com.doozycod.fleetoptics.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.fleetoptics.Interface.CallbackListener;
import com.doozycod.fleetoptics.Model.GetEmployeeModel;
import com.doozycod.fleetoptics.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    Context context;
    List<GetEmployeeModel.employees> employeesList;
    CallbackListener callbackListener;

    public RecyclerAdapter(Context context, List<GetEmployeeModel.employees> employeesList, CallbackListener callbackListener) {
        this.context = context;
        this.employeesList = employeesList;
        this.callbackListener = callbackListener;
    }
    @NonNull
    @Override
    public RecyclerAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view, parent, false);
        return new RecyclerHolder(view);
    }

//      on search user name update list of employees
    public void updateList(List<GetEmployeeModel.employees> list) {
        employeesList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.RecyclerHolder holder, final int position) {
        holder.employeeName.setText(employeesList.get(position).getFull_name());
        holder.employeeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, employeesList.get(position).getFull_name()+" is selected!", Toast.LENGTH_SHORT).show();
                callbackListener.onResultListener(employeesList.get(position).getFull_name(), employeesList.get(position).getId());
            }
        });
    }

    //  size of list employee
    @Override
    public int getItemCount() {
        return employeesList.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView employeeName;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.emp_name);
        }
    }
}
