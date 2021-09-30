package com.org.zapayapp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WalletModel {
    @SerializedName("balance_details")
    @Expose
    private BalanceDetailsModel balanceDetailsModel;
    @SerializedName("transaction_history")
    @Expose
    private List<WalletTransactionModel> walletTransactionModelList = null;

    public BalanceDetailsModel getBalanceDetailsModel() {
        return balanceDetailsModel;
    }

    public List<WalletTransactionModel> getWalletTransactionModelList() {
        return walletTransactionModelList;
    }
}
