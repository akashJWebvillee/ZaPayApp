package com.org.zapayapp.model;

public class PabackModel {
    private String payDate;
    private boolean isAddDate;

    public PabackModel(String payDate, boolean isAddDate) {
        this.payDate = payDate;
        this.isAddDate = isAddDate;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public boolean isAddDate() {
        return isAddDate;
    }

    public void setAddDate(boolean addDate) {
        isAddDate = addDate;
    }
}
