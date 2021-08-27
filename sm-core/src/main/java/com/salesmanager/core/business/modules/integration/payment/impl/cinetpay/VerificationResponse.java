package com.salesmanager.core.business.modules.integration.payment.impl.cinetpay;

public class VerificationResponse {

    private String cpm_site_id;
    private String signature;
    private long cpm_amount;
    private String cpm_trans_date;
    private String cpm_trans_id;
    private String cpm_custom;
    private String cpm_currency;
    private String cpm_payid;
    private String cpm_payment_date;
    private String cpm_payment_time;
    private String cpm_error_message;
    private String payment_method;
    private String cpm_phone_prefixe;
    private String cel_phone_num;
    private String cpm_ipn_ack;
    private String created_at;
    private String updated_at;
    private String cpm_result;
    private String cpm_trans_status;
    private String cpm_designation;
    private String buyer_name;

    public String getCpm_site_id() {
        return cpm_site_id;
    }

    public void setCpm_site_id(String cpm_site_id) {
        this.cpm_site_id = cpm_site_id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public long getCpm_amount() {
        return cpm_amount;
    }

    public void setCpm_amount(long cpm_amount) {
        this.cpm_amount = cpm_amount;
    }

    public String getCpm_trans_date() {
        return cpm_trans_date;
    }

    public void setCpm_trans_date(String cpm_trans_date) {
        this.cpm_trans_date = cpm_trans_date;
    }

    public String getCpm_trans_id() {
        return cpm_trans_id;
    }

    public void setCpm_trans_id(String cpm_trans_id) {
        this.cpm_trans_id = cpm_trans_id;
    }

    public String getCpm_custom() {
        return cpm_custom;
    }

    public void setCpm_custom(String cpm_custom) {
        this.cpm_custom = cpm_custom;
    }

    public String getCpm_currency() {
        return cpm_currency;
    }

    public void setCpm_currency(String cpm_currency) {
        this.cpm_currency = cpm_currency;
    }

    public String getCpm_payid() {
        return cpm_payid;
    }

    public void setCpm_payid(String cpm_payid) {
        this.cpm_payid = cpm_payid;
    }

    public String getCpm_payment_date() {
        return cpm_payment_date;
    }

    public void setCpm_payment_date(String cpm_payment_date) {
        this.cpm_payment_date = cpm_payment_date;
    }

    public String getCpm_payment_time() {
        return cpm_payment_time;
    }

    public void setCpm_payment_time(String cpm_payment_time) {
        this.cpm_payment_time = cpm_payment_time;
    }

    public String getCpm_error_message() {
        return cpm_error_message;
    }

    public void setCpm_error_message(String cpm_error_message) {
        this.cpm_error_message = cpm_error_message;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getCpm_phone_prefixe() {
        return cpm_phone_prefixe;
    }

    public void setCpm_phone_prefixe(String cpm_phone_prefixe) {
        this.cpm_phone_prefixe = cpm_phone_prefixe;
    }

    public String getCel_phone_num() {
        return cel_phone_num;
    }

    public void setCel_phone_num(String cel_phone_num) {
        this.cel_phone_num = cel_phone_num;
    }

    public String getCpm_ipn_ack() {
        return cpm_ipn_ack;
    }

    public void setCpm_ipn_ack(String cpm_ipn_ack) {
        this.cpm_ipn_ack = cpm_ipn_ack;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCpm_result() {
        return cpm_result;
    }

    public void setCpm_result(String cpm_result) {
        this.cpm_result = cpm_result;
    }

    public String getCpm_trans_status() {
        return cpm_trans_status;
    }

    public void setCpm_trans_status(String cpm_trans_status) {
        this.cpm_trans_status = cpm_trans_status;
    }

    public String getCpm_designation() {
        return cpm_designation;
    }

    public void setCpm_designation(String cpm_designation) {
        this.cpm_designation = cpm_designation;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    @Override
    public String toString() {
        return "VerificationResponse{" +
                "cpm_site_id='" + cpm_site_id + '\'' +
                ", signature='" + signature + '\'' +
                ", cpm_amount='" + cpm_amount + '\'' +
                ", cpm_trans_date='" + cpm_trans_date + '\'' +
                ", cpm_trans_id='" + cpm_trans_id + '\'' +
                ", cpm_custom='" + cpm_custom + '\'' +
                ", cpm_currency='" + cpm_currency + '\'' +
                ", cpm_payid='" + cpm_payid + '\'' +
                ", cpm_payment_date='" + cpm_payment_date + '\'' +
                ", cpm_payment_time='" + cpm_payment_time + '\'' +
                ", cpm_error_message='" + cpm_error_message + '\'' +
                ", payment_method='" + payment_method + '\'' +
                ", cpm_phone_prefixe='" + cpm_phone_prefixe + '\'' +
                ", cel_phone_num='" + cel_phone_num + '\'' +
                ", cpm_ipn_ack='" + cpm_ipn_ack + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", cpm_result='" + cpm_result + '\'' +
                ", cpm_trans_status='" + cpm_trans_status + '\'' +
                ", cpm_designation='" + cpm_designation + '\'' +
                ", buyer_name='" + buyer_name + '\'' +
                '}';
    }
}
