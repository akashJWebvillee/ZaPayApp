package com.org.zapayapp.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class ChatMessageModel {
    public static final int RECEIVED_MSG = 0;
    public static final int SEND_MSG = 1;

    public ChatMessageModel() {
    }

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("sender_id")
    @Expose
    public String senderId;
    @SerializedName("receiver_id")
    @Expose
    public String receiverId;
    @SerializedName("transaction_request_id")
    @Expose
    public String transactionRequestId;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("created_at")
    @Expose
    public long createdAt;
    @SerializedName("sender_first_name")
    @Expose
    public String senderFirstName;
    @SerializedName("sender_last_name")
    @Expose
    public String senderLastName;
    @SerializedName("sender_profile_image")
    @Expose
    public String senderProfileImage;
    @SerializedName("receiver_first_name")
    @Expose
    public String receiverFirstName;
    @SerializedName("receiver_last_name")
    @Expose
    public String receiverLastName;
    @SerializedName("receiver_profile_image")
    @Expose
    public String receiverProfileImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setTransactionRequestId(String transactionRequestId) {
        this.transactionRequestId = transactionRequestId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public void setSenderProfileImage(String senderProfileImage) {
        this.senderProfileImage = senderProfileImage;
    }

    public void setReceiverFirstName(String receiverFirstName) {
        this.receiverFirstName = receiverFirstName;
    }

    public void setReceiverLastName(String receiverLastName) {
        this.receiverLastName = receiverLastName;
    }

    public void setReceiverProfileImage(String receiverProfileImage) {
        this.receiverProfileImage = receiverProfileImage;
    }

    public static Comparator<ChatMessageModel> timeComparator = new Comparator<ChatMessageModel>() {

        public int compare(ChatMessageModel s1, ChatMessageModel s2) {
            long sendOn1 = s1.getCreatedAt();
            long sendOn2 = s2.getCreatedAt();
            //ascending order
            return Long.compare(sendOn1, sendOn2);
        }
    };
}
