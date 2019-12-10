package com.doozycod.fleetoptics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultModel {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("type")
    @Expose
    String type;
    @SerializedName("employee_contact")
    @Expose
    String employee_contact;

    public String getEmployee_contact() {
        return employee_contact;
    }

    public void setEmployee_contact(String employee_contact) {
        this.employee_contact = employee_contact;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
