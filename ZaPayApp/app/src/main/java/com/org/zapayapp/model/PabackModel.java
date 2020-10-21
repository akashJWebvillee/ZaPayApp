package com.org.zapayapp.model;

public class PabackModel {
    private String payDate;
    private boolean isAddDate;
    private boolean isDateEpockFormate;

    public PabackModel(String payDate, boolean isAddDate,boolean isDateEpockFormate) {
        this.payDate = payDate;
        this.isAddDate = isAddDate;
        this.isDateEpockFormate = isDateEpockFormate;
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

    public boolean isDateEpockFormate() {
        return isDateEpockFormate;
    }
}
