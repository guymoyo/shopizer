package com.salesmanager.core.business.modules.integration.payment.impl.cinetpay;

public class CinetPayResponseData {

    private String payment_token;
    private String payment_url;

    public String getPayment_token() {
        return payment_token;
    }

    public void setPayment_token(String payment_token) {
        this.payment_token = payment_token;
    }

    public String getPayment_url() {
        return payment_url;
    }

    public void setPayment_url(String payment_url) {
        this.payment_url = payment_url;
    }
}
