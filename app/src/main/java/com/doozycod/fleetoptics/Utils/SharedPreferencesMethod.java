package com.doozycod.fleetoptics.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesMethod {
    private Context context;

    public SharedPreferencesMethod(Context context) {
        this.context = context;
    }

    public void spInsert(String servername, String serverAddress, String serverPassword, String log_level) {
        SharedPreferences sp = context.getSharedPreferences("FleetOptics", Context.MODE_PRIVATE);
        SharedPreferences.Editor sp_editior = sp.edit();
        sp_editior.putString("serverUsername", servername);
        sp_editior.putString("serverAddress", serverAddress);
        sp_editior.putString("serverPassword", serverPassword);
        sp_editior.putString("log_level", log_level);
        sp_editior.apply();
    }


    public String getUserName() {
        SharedPreferences sp = context.getSharedPreferences("FleetOptics", Context.MODE_PRIVATE);
        return sp.getString("serverUsername", "");
    }

    public String getPassword() {
        SharedPreferences sp = context.getSharedPreferences("FleetOptics", Context.MODE_PRIVATE);
        return sp.getString("serverPassword", "");
    }

    public String getServerAddress() {
        SharedPreferences sp = context.getSharedPreferences("FleetOptics", Context.MODE_PRIVATE);
        return sp.getString("serverAddress", "");
    }

    public String getLogLevel() {
        SharedPreferences sp = context.getSharedPreferences("FleetOptics", Context.MODE_PRIVATE);
        return sp.getString("log_level", "");
    }

}
