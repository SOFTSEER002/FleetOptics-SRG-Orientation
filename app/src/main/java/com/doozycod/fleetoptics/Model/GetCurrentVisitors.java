package com.doozycod.fleetoptics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCurrentVisitors {


    public List<GetCurrentVisitors.visitors> getVisitors() {
        return visitors;
    }

    public void setVisitors(List<GetCurrentVisitors.visitors> visitors) {
        this.visitors = visitors;
    }

    public GetCurrentVisitors.visitors getVisitorList() {
        return visitorList;
    }

    public void setVisitorList(GetCurrentVisitors.visitors visitorList) {
        this.visitorList = visitorList;
    }

    @SerializedName("visitors")
    @Expose
    private List<GetCurrentVisitors.visitors> visitors = null;


    private GetCurrentVisitors.visitors visitorList;

    public class visitors {
        @SerializedName("id")
        @Expose
        String id;
        @SerializedName("checkin_type")
        @Expose
        String checkin_type;
        @SerializedName("purpose_of_visit")
        @Expose
        String purpose_of_visit;
        @SerializedName("full_name")
        @Expose
        String full_name;
        @SerializedName("company_name")
        @Expose
        String company_name;
        @SerializedName("email_address")
        @Expose
        String email_address;
        @SerializedName("meet_to_whom")
        @Expose
        String meet_to_whom;
        @SerializedName("employee_name")
        @Expose
        String employee_name;
        @SerializedName("phone_no")
        @Expose
        String phone_no;
        @SerializedName("image")
        @Expose
        String image;
        @SerializedName("timestamp")
        @Expose
        String timestamp;
        @SerializedName("signout_timestamp")
        @Expose
        String signout_timestamp;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCheckin_type() {
            return checkin_type;
        }

        public void setCheckin_type(String checkin_type) {
            this.checkin_type = checkin_type;
        }

        public String getPurpose_of_visit() {
            return purpose_of_visit;
        }

        public void setPurpose_of_visit(String purpose_of_visit) {
            this.purpose_of_visit = purpose_of_visit;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getEmail_address() {
            return email_address;
        }

        public void setEmail_address(String email_address) {
            this.email_address = email_address;
        }

        public String getMeet_to_whom() {
            return meet_to_whom;
        }

        public void setMeet_to_whom(String meet_to_whom) {
            this.meet_to_whom = meet_to_whom;
        }

        public String getEmployee_name() {
            return employee_name;
        }

        public void setEmployee_name(String employee_name) {
            this.employee_name = employee_name;
        }

        public String getPhone_no() {
            return phone_no;
        }

        public void setPhone_no(String phone_no) {
            this.phone_no = phone_no;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSignout_timestamp() {
            return signout_timestamp;
        }

        public void setSignout_timestamp(String signout_timestamp) {
            this.signout_timestamp = signout_timestamp;
        }


    }
}
