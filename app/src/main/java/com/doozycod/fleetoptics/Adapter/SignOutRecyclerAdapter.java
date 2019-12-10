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
import com.doozycod.fleetoptics.Model.GetCurrentVisitors;
import com.doozycod.fleetoptics.R;

import java.util.ArrayList;
import java.util.List;

public class SignOutRecyclerAdapter extends RecyclerView.Adapter<SignOutRecyclerAdapter.SignHolder> {

    Context context;
    //    callback listener to add user
    CallbackListener callbackListener;
//    visitor list data for signout
    List<GetCurrentVisitors.visitors> visitorsList = new ArrayList<>();
    public SignOutRecyclerAdapter(Context context, CallbackListener callbackListener, List<GetCurrentVisitors.visitors> visitorsList) {
        this.context = context;
        this.callbackListener = callbackListener;
        this.visitorsList = visitorsList;
    }
    @NonNull
    @Override
    public SignOutRecyclerAdapter.SignHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expenditors_recycler_v, parent, false);
        return new SignHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignOutRecyclerAdapter.SignHolder holder, final int position) {
        holder.emp_nameSignout.setText(visitorsList.get(position).getFull_name());
        holder.emp_nameSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, visitorsList.get(position).getFull_name() + " is selected!", Toast.LENGTH_SHORT).show();
                callbackListener.onResultListener(visitorsList.get(position).getFull_name(), visitorsList.get(position).getEmail_address());
            }
        });
    }

//    show only selected user or user by name update list
    public void updateList(List<GetCurrentVisitors.visitors> list) {
        visitorsList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return visitorsList.size();
    }

    public class SignHolder extends RecyclerView.ViewHolder {
        TextView emp_nameSignout;

        public SignHolder(@NonNull View itemView) {
            super(itemView);
            emp_nameSignout = itemView.findViewById(R.id.emp_nameSignout);
        }
    }
}
