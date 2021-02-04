package com.org.zapayapp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class TransactionModel implements Serializable {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("from_id")
    @Expose
    public String fromId;
    @SerializedName("to_id")
    @Expose
    public String toId;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("total_amount")
    @Expose
    public String totalAmount;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("terms_type")
    @Expose
    public String termsType;
    @SerializedName("terms_value")
    @Expose
    public String termsValue;
    @SerializedName("no_of_payment")
    @Expose
    public String noOfPayment;
    @SerializedName("pay_date")
    @Expose
    public String payDate;
    @SerializedName("updated_by")
    @Expose
    public String updatedBy;
    @SerializedName("request_by")
    @Expose
    public String requestBy;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("profile_image")
    @Expose
    public String profileImage;

    @SerializedName("average_rating")
    @Expose
    public String average_rating;

    @SerializedName("pay_date_update_status_is_pending")
    @Expose
    public String pay_date_update_status_is_pending;


    @SerializedName("parent_id")
    @Expose
    public String parent_id;

    @SerializedName("is_negotiate_after_accept")
    @Expose
    public String is_negotiate_after_accept;




    public String getPay_date_update_status_is_pending() {
        return pay_date_update_status_is_pending;
    }

    public String getId() {
        return id;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public String getAmount() {
        return amount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getTermsType() {
        return termsType;
    }

    public String getTermsValue() {
        return termsValue;
    }

    public String getNoOfPayment() {
        return noOfPayment;
    }

    public String getPayDate() {
        return payDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public String getParent_id() {
        return parent_id;
    }

    public String getIs_negotiate_after_accept() {
        return is_negotiate_after_accept;
    }
}
