package com.salesmanager.core.business.modules.integration.payment.impl.cinetpay;

public class VerificationResponse {

    private String code;
    private String message;
    private String api_response_id;
    private VerificationRespData data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getApi_response_id() {
        return api_response_id;
    }

    public void setApi_response_id(String api_response_id) {
        this.api_response_id = api_response_id;
    }

    public VerificationRespData getData() {
        return data;
    }

    public void setData(VerificationRespData data) {
        this.data = data;
    }
}
