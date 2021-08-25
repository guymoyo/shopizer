package com.salesmanager.core.business.modules.integration.payment.impl;

import com.salesmanager.core.business.modules.integration.payment.impl.cinetpay.CinetPayRequest;
import com.salesmanager.core.business.modules.integration.payment.impl.cinetpay.CinetPayResponse;
import com.salesmanager.core.business.modules.integration.payment.impl.cinetpay.VerificationRequest;
import com.salesmanager.core.business.modules.integration.payment.impl.cinetpay.VerificationResponse;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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

        CinetPayRequest cinetPayRequest = new CinetPayRequest();
        cinetPayRequest.setAmount(amountToPay);
        cinetPayRequest.setApikey(API_KEY);
        cinetPayRequest.setSite_id(SITE_ID);
        cinetPayRequest.setTransaction_id(transactionId);
        cinetPayRequest.setDescription(transactionId);
        cinetPayRequest.setCustomer_name(lastName);
        cinetPayRequest.setCustomer_surname(firstName);
        cinetPayRequest.setNotify_url("https://www.djome.com/shop/cinetpay/notify");
        cinetPayRequest.setReturn_url("https://www.djome.com/shop/cinetpay/checkout.html");
        cinetPayRequest.setCurrency(getCurrency(country));


        CinetPayResponse paymentInitResp = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            paymentInitResp = restTemplate.postForObject("https://api-checkout.cinetpay.com/v2/payment", cinetPayRequest, CinetPayResponse.class);
        } catch (RestClientException e) {
            IntegrationException te = new IntegrationException(
                    "Can't process cinetpay, try later");
            te.setExceptionType(IntegrationException.TRANSACTION_EXCEPTION);
            te.setMessageCode("message.payment.error");
            te.setErrorCode(IntegrationException.TRANSACTION_EXCEPTION);
            throw te;
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionDate(new Date());
        transaction.setTransactionType(TransactionType.INIT);
        transaction.setPaymentType(PaymentType.CINETPAY);

        //transaction.setDetails(String.valueOf(orderId));
        transaction.getTransactionDetails().put("transactionId", transactionId);
        transaction.getTransactionDetails().put("customer_firstname", firstName);
        transaction.getTransactionDetails().put("customer_lastname", lastName);
        transaction.getTransactionDetails().put("customer_country", country);
        transaction.getTransactionDetails().put("PAYMENT_TOKEN", paymentInitResp.getData().getPayment_token());
        transaction.getTransactionDetails().put("PAYMENT_URL", paymentInitResp.getData().getPayment_url());

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
    public Transaction authorizeAndCapture(MerchantStore store, Customer customer, List<ShoppingCartItem> items, BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {

        String paymentToken = payment.getPaymentMetaData().get("PAYMENT_TOKEN");

        if(StringUtils.isBlank(paymentToken)) {
            IntegrationException te = new IntegrationException(
                    "Can't process CinetPay, missing  token");
            te.setExceptionType(IntegrationException.TRANSACTION_EXCEPTION);
            te.setMessageCode("message.payment.error");
            te.setErrorCode(IntegrationException.TRANSACTION_EXCEPTION);
            throw te;
        }

        VerificationRequest verificationRequest = new VerificationRequest();
        verificationRequest.setToken(paymentToken);
        verificationRequest.setApikey(CinetPayPayment.API_KEY);
        verificationRequest.setSite_id(CinetPayPayment.SITE_ID);

        RestTemplate restTemplate = new RestTemplate();
        VerificationResponse verificationResponse = null;

        try {
            verificationResponse = restTemplate.postForObject("https://api-checkout.cinetpay.com/v2/payment/check", verificationRequest, VerificationResponse.class);
            if (!"SUCCES".equals(verificationResponse.getMessage())){
                IntegrationException te = new IntegrationException(
                        "Can't process CinetPay, error cinetpay");
                te.setExceptionType(IntegrationException.TRANSACTION_EXCEPTION);
                te.setMessageCode("message.payment.error");
                te.setErrorCode(IntegrationException.TRANSACTION_EXCEPTION);
                throw te;
            }
        } catch (RestClientException e) {
            IntegrationException te = new IntegrationException(
                    "Can't process CinetPay, error cinetpay");
            te.setExceptionType(IntegrationException.TRANSACTION_EXCEPTION);
            te.setMessageCode("message.payment.error");
            te.setErrorCode(IntegrationException.TRANSACTION_EXCEPTION);
            throw te;
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionDate(new Date());
        transaction.setTransactionType(TransactionType.AUTHORIZECAPTURE);
        transaction.setPaymentType(PaymentType.CINETPAY);
        transaction.getTransactionDetails().put("PAYMENT_TOKEN", paymentToken);
        transaction.setDetails(paymentToken);

        return transaction;
    }

    @Override
    public Transaction refund(boolean partial, MerchantStore store, Transaction transaction, Order order, BigDecimal amount, IntegrationConfiguration configuration, IntegrationModule module) throws IntegrationException {
        return null;
    }

    private String getCurrency(String country) {
        if(country=="CM") {
            return "XAF";
        }
        if(country=="BF") {
            return "XOF";
        }
        if(country=="CI") {
            return "XOF";
        }
        if(country=="ML") {
            return "XOF";
        }
        if(country=="SN") {
            return "XOF";
        }
        if(country=="TG") {
            return "XOF";
        }
        return "XAF";
    }
}
