package com.oppo.sfamanagement.model;

/**
 * Created by AllSmart-LT008 on 12/15/2016.
 */

public class Response {

    private String strResponce="";
    private String strResponceCode="";
    private String strResponceMessage="";
    private String strResponceComments="";

    public String getResponce() {
        return strResponce;
    }

    public void setResponce(String strResponce) {
        this.strResponce = strResponce;
    }

    public String getResponceCode() {
        return strResponceCode;
    }

    public void setResponceCode(String strResponceCode) {
        this.strResponceCode = strResponceCode;
    }

    public String getResponceMessage() {
        return strResponceMessage;
    }

    public void setResponceMessage(String strResponceMessage) {
        this.strResponceMessage = strResponceMessage;
    }

    public String getResponceComments() {
        return strResponceComments;
    }

    public void setResponceComments(String strResponceComments) {
        this.strResponceComments = strResponceComments;
    }
}
