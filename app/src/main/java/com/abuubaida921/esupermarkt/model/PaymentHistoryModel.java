package com.abuubaida921.esupermarkt.model;

public class PaymentHistoryModel {
    String amount, order_id, payment_id, txn_id, user_id;
    Long paid_at;

    public PaymentHistoryModel() {
    }

    public PaymentHistoryModel(String amount, String order_id, String payment_id, String txn_id, String user_id, Long paid_at) {
        this.amount = amount;
        this.order_id = order_id;
        this.payment_id = payment_id;
        this.txn_id = txn_id;
        this.user_id = user_id;
        this.paid_at = paid_at;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(String txn_id) {
        this.txn_id = txn_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Long getPaid_at() {
        return paid_at;
    }

    public void setPaid_at(Long paid_at) {
        this.paid_at = paid_at;
    }
}
