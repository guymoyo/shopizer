package com.salesmanager.test.payment;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.salesmanager.core.business.exception.ServiceException;
import org.apache.commons.logging.Log;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class cinetPayTest {

    private static final Logger log = LoggerFactory.getLogger(cinetPayTest.class);

    @Test
    public void testCinetpay() throws ServiceException {

        Unirest.setTimeouts(0, 0);
        HttpResponse<JsonNode> response;
        try {
            response = Unirest.post("https://api.cinetpay.com/v1/?method=checkPayStatus")
                    .field("apikey", "13912947265fa65db347e7a4.83208927")
                    .field("cpm_site_id", "479567")
                    .field("cpm_trans_id", "wcLqtuBFiG")
                    .asJson();

            JSONObject jsonObjectBody = response.getBody().getObject();

            JSONObject transactionJsonObj = jsonObjectBody.getJSONObject("transaction");

            String cpm_result = transactionJsonObj.getString("cpm_result");
            String cpm_amount = transactionJsonObj.getString("cpm_amount");

            if(!"00".equals(cpm_result)) {
                ServiceException te = new ServiceException(
                        "Can't process CinetPay, error cinetpay");
                te.setExceptionType(ServiceException.EXCEPTION_PAYMENT_DECLINED);
                te.setMessageCode("message.payment.error");
                throw te;
            }

            if(BigDecimal.valueOf(100).compareTo(BigDecimal.valueOf(Long.valueOf(cpm_amount)))!=0) {
                ServiceException te = new ServiceException(
                        "Can't process CinetPay, error cinetpay");
                te.setExceptionType(ServiceException.EXCEPTION_PAYMENT_DECLINED);
                te.setMessageCode("message.payment.error");
                throw te;
            }

        } catch (UnirestException e) {
            log.error(e.toString());
        }
    }
}
