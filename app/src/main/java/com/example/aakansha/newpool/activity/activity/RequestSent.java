package com.example.aakansha.newpool.activity.activity;

/**
 * Created by shubhilohani on 7/12/17.
 */

public class RequestSent {



        public String employee_id;
        public String address;
        public String employee_name;
//        public String notify;


        public RequestSent(String employee_id, String employee_name,
                      String address) {

            this.employee_id = employee_id;
            this.employee_name = employee_name;
            this.address = address;
            }

    public String getEmployee_id() {
        return employee_id;
    }

    public String getEmployee_name(){
        return employee_name;
    }
    public String getAddress() {
        return address;
    }


}


