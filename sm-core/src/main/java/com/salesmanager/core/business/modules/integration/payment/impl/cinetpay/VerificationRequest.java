package com.salesmanager.core.business.modules.integration.payment.impl.cinetpay;

public class VerificationRequest {


    private String apikey;
    private String cpm_site_id;
    private String cpm_trans_id;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getCpm_site_id() {
        return cpm_site_id;
    }

    public void setCpm_site_id(String cpm_site_id) {
        this.cpm_site_id = cpm_site_id;
    }

    public String getCpm_trans_id() {
        return cpm_trans_id;
    }

    public void setCpm_trans_id(String cpm_trans_id) {
        this.cpm_trans_id = cpm_trans_id;
    }
}
