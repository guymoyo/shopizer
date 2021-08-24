package com.salesmanager.core.business.modules.integration.payment.impl.cinetpay;

public class VerificationRespData {

    private String operator_id;
    private String payment_method;
    private String payment_date;
    private String phone_number;
    private String phone_prefix;

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPhone_prefix() {
        return phone_prefix;
    }

    public void setPhone_prefix(String phone_prefix) {
        this.phone_prefix = phone_prefix;
    }
}
