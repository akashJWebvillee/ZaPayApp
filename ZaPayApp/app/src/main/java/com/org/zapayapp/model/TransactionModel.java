package com.org.zapayapp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class TransactionModel implements Serializable{
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

    @SerializedName("commission_charges_detail")
    @Expose
    public String commission_charges_detail;

    @SerializedName("admin_commission_from_lender")
    @Expose
    public String admin_commission_from_lender;

    @SerializedName("admin_commission_from_borrower")
    @Expose
    public String admin_commission_from_borrower;

    @SerializedName("pay_dates_list")
    @Expose
    private List<DateModel> payDatesList = null;

    @SerializedName("pdf_details")
    @Expose
    private List<AgreementPdfDetailModel> agreementPdfDetailModelList = null;


    @SerializedName("sender_first_name")
    @Expose
    public String sender_first_name;
    @SerializedName("sender_last_name")
    @Expose
    public String sender_last_name;

    @SerializedName("receiver_first_name")
    @Expose
    public String receiver_first_name;
    @SerializedName("receiver_last_name")
    @Expose
    public String receiver_last_name;


    @SerializedName("sender_average_rating")
    @Expose
    public String sender_average_rating;
    @SerializedName("receiver_average_rating")
    @Expose
    public String receiver_average_rating;

    @SerializedName("sender_profile_image")
    @Expose
    public String sender_profile_image;

    @SerializedName("receiver_profile_image")
    @Expose
    public String receiver_profile_image;

    @SerializedName("cancel_by_first_name")
    @Expose
    public String cancel_by_first_name;
    @SerializedName("cancel_by_last_name")
    @Expose
    public String cancel_by_last_name;

    @SerializedName("cancel_by_profile_image")
    @Expose
    public String cancel_by_profile_image;

    @SerializedName("is_already_rated")
    @Expose
    public String is_already_rated;

    @SerializedName("rating_by_user")
    @Expose
    public String rating_by_user;

    @SerializedName("due_amount")
    @Expose
    public String due_amount;

    @SerializedName("child_request_is_accepted")
    @Expose
    public String child_request_is_accepted;
    @SerializedName("child_amount")
    @Expose
    public String child_amount;
    @SerializedName("child_total_amount")
    @Expose
    public String child_total_amount;
    @SerializedName("child_admin_commission_from_borrower")
    @Expose
    public String child_admin_commission_from_borrower;


    public String getIs_already_rated() {
        return is_already_rated;
    }

    public String getRating_by_user() {
        return rating_by_user;
    }

    public List<AgreementPdfDetailModel> getAgreementPdfDetailModelList() {
        return agreementPdfDetailModelList;
    }

    public List<DateModel> getPayDatesList() {
        return payDatesList;
    }

    public String getAdmin_commission_from_lender() {
        return admin_commission_from_lender;
    }

    public String getAdmin_commission_from_borrower() {
        return admin_commission_from_borrower;
    }

    public String getCommission_charges_detail() {
        return commission_charges_detail;
    }

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

    public String getSender_first_name() {
        return sender_first_name;
    }

    public String getSender_last_name() {
        return sender_last_name;
    }

    public String getReceiver_first_name() {
        return receiver_first_name;
    }

    public String getReceiver_last_name() {
        return receiver_last_name;
    }


    public String getSender_average_rating() {
        return sender_average_rating;
    }

    public String getReceiver_average_rating() {
        return receiver_average_rating;
    }

    public String getSender_profile_image() {
        return sender_profile_image;
    }

    public String getReceiver_profile_image() {
        return receiver_profile_image;
    }

    public String getCancel_by_first_name() {
        return cancel_by_first_name;
    }

    public String getCancel_by_last_name() {
        return cancel_by_last_name;
    }

    public String getCancel_by_profile_image() {
        return cancel_by_profile_image;
    }

    public String getDue_amount() {
        return due_amount;
    }

    public String getChild_request_is_accepted() {
        return child_request_is_accepted;
    }

    public String getChild_amount() {
        return child_amount;
    }

    public String getChild_total_amount() {
        return child_total_amount;
    }

    public String getChild_admin_commission_from_borrower() {
        return child_admin_commission_from_borrower;
    }
}
