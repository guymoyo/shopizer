package com.salesmanager.core.business.modules.integration.payment.impl.cinetpay;

public class VerificationRequest {


    private String apikey;
    private String site_id;
    private String token;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
