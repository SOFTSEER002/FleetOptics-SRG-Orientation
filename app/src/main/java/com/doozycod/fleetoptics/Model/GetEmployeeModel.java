package com.doozycod.fleetoptics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetEmployeeModel {
    public List<GetEmployeeModel.employees> getEmployees() {
        return employees;
    }

    public void setEmployees(List<GetEmployeeModel.employees> employees) {
        this.employees = employees;
    }

    @SerializedName("employees")
    @Expose
    private List<employees> employees=null;

    public GetEmployeeModel.employees getEmployeesList() {
        return employeesList;
    }

    public void setEmployeesList(GetEmployeeModel.employees employeesList) {
        this.employeesList = employeesList;
    }

    private employees employeesList;

    public class employees{
        @SerializedName("id")
        @Expose
        String id;
        @SerializedName("comapny_name")
        @Expose
        String comapny_name;
        @SerializedName("full_name")
        @Expose
        String full_name;
        @SerializedName("email_address")
        @Expose
        String email_address;
        @SerializedName("phone_no")
        @Expose
        String phone_no;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getComapny_name() {
            return comapny_name;
        }

        public void setComapny_name(String comapny_name) {
            this.comapny_name = comapny_name;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getEmail_address() {
            return email_address;
        }

        public void setEmail_address(String email_address) {
            this.email_address = email_address;
        }

        public String getPhone_no() {
            return phone_no;
        }

        public void setPhone_no(String phone_no) {
            this.phone_no = phone_no;
        }
    }
}
