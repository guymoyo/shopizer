package com.salesmanager.core.business.modules.integration.payment.impl.cinetpay;

public class CinetPayResponse {

    private String code;
    private String message;
    private String description;
    private CinetPayResponseData data;
    private String api_response_id;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CinetPayResponseData getData() {
        return data;
    }

    public void setData(CinetPayResponseData data) {
        this.data = data;
    }

    public String getApi_response_id() {
        return api_response_id;
    }

    public void setApi_response_id(String api_response_id) {
        this.api_response_id = api_response_id;
    }
}
