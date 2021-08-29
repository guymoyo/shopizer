package com.salesmanager.core.business.modules.integration.payment.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.payments.Payment;
import com.salesmanager.core.model.payments.PaymentType;
import com.salesmanager.core.model.payments.Transaction;
import com.salesmanager.core.model.payments.TransactionType;
import com.salesmanager.core.model.shoppingcart.ShoppingCartItem;
import com.salesmanager.core.model.system.IntegrationConfiguration;
import com.salesmanager.core.model.system.IntegrationModule;
import com.salesmanager.core.modules.integration.IntegrationException;
import com.salesmanager.core.modules.integration.payment.model.PaymentModule;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CinetPayPayment implements PaymentModule {

    public final static String API_KEY = "13912947265fa65db347e7a4.83208927";
    public final static String SITE_ID = "479567";


    @Override
    public void validateModuleConfiguration(IntegrationConfiguration integrationConfiguration, MerchantStore store) throws IntegrationException {

    }

    @Override
    public Transaction initTransaction(MerchantStore store, Customer customer, BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module, String country, String firstName, String lastName, Long orderId) throws IntegrationException {

        BigDecimal priceDollars = store.getPriceDollars();
        if(priceDollars==null){
            throw new IllegalArgumentException("set store.getPriceDollars");
        }
        long amountToPay = amount.multiply(priceDollars).longValue();
        String transactionId = RandomStringUtils.randomAlphabetic(10);
        String currency = getCurrency(country);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionDate(new Date());
        transaction.setTransactionType(TransactionType.INIT);
        transaction.setPaymentType(PaymentType.CINETPAY);

        //transaction.setDetails(String.valueOf(orderId));
        transaction.getTransactionDetails().put("customer_firstname", firstName);
        transaction.getTransactionDetails().put("customer_lastname", lastName);
        transaction.getTransactionDetails().put("customer_country", country);
        transaction.getTransactionDetails().put("currency", currency);
        transaction.getTransactionDetails().put("amount", String.valueOf(amountToPay));
        transaction.getTransactionDetails().put("trans_id", transactionId);

        return transaction;
    }

    @Override
    public Transaction authorize(MerchantStore store, Customer customer, List<ShoppingCartItem> items, BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {
        return null;
    }

    @Override
    public Transaction capture(MerchantStore store, Customer customer, Order order, Transaction capturableTransaction, IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {
        return null;
    }

    @Override
    public Transaction authorizeAndCapture(MerchantStore store, Customer customer, List<ShoppingCartItem> items, BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module) throws ServiceException {

        String trans_id = payment.getPaymentMetaData().get("trans_id");

        if(StringUtils.isBlank(trans_id)) {
            IntegrationException te = new IntegrationException(
                    "Can't process CinetPay, missing  trans_id");
            te.setExceptionType(IntegrationException.TRANSACTION_EXCEPTION);
            te.setMessageCode("message.payment.error");
            te.setErrorCode(IntegrationException.TRANSACTION_EXCEPTION);
            throw te;
        }


        Unirest.setTimeouts(0, 0);
        HttpResponse<JsonNode> response;
        try {
            response = Unirest.post("https://api.cinetpay.com/v1/?method=checkPayStatus")
                    .field("apikey", API_KEY)
                    .field("cpm_site_id", SITE_ID)
                    .field("cpm_trans_id", trans_id)
                    .asJson();
        } catch (UnirestException e) {
            ServiceException te = new ServiceException(
                    "Can't process CinetPay, error cinetpay");
            te.setExceptionType(ServiceException.EXCEPTION_PAYMENT_DECLINED);
            te.setMessageCode("message.payment.error");
            throw te;
        }

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

            if(amount.compareTo(BigDecimal.valueOf(Long.valueOf(cpm_amount)))!=0) {
                ServiceException te = new ServiceException(
                        "Can't process CinetPay, error cinetpay");
                te.setExceptionType(ServiceException.EXCEPTION_PAYMENT_DECLINED);
                te.setMessageCode("message.payment.error");
                throw te;
            }

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionDate(new Date());
        transaction.setTransactionType(TransactionType.AUTHORIZECAPTURE);
        transaction.setPaymentType(PaymentType.CINETPAY);
        transaction.getTransactionDetails().put("trans_id", trans_id);
        transaction.getTransactionDetails().put("cinitpaytransaction", transactionJsonObj.toString());

        return transaction;
    }

    @Override
    public Transaction refund(boolean partial, MerchantStore store, Transaction transaction, Order order, BigDecimal amount, IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {
        return null;
    }

    private String getCurrency(String country) {
        if("CM".equals(country)) {
            return "XAF";
        }
        if("BF".equals(country)) {
            return "XOF";
        }
        if("CI".equals(country)) {
            return "XOF";
        }
        if("ML".equals(country)) {
            return "XOF";
        }
        if("SN".equals(country)) {
            return "XOF";
        }
        if("TG".equals(country)) {
            return "XOF";
        }
        /*if("CD".equals(country)) {
            return "CDF";
        }*/
        return "XAF";
    }
}
