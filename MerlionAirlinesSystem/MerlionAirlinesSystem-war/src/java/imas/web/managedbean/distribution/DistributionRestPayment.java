/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.distribution;

import com.paypal.api.payments.Address;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.CreditCard;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.FundingInstrument;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
/**
 *
 * @author ruicai
 */
@Named(value = "distributionRestPayment")
@ViewScoped
public class DistributionRestPayment implements Serializable{

    /**
     * Creates a new instance of DistributionRestPayment
     */
    public DistributionRestPayment() {
    }

    /**
     *
     * @throws PayPalRESTException
     */
   
    public void restPay() throws PayPalRESTException  { 
        String clientID = "AWvE0BAwWOfvkR-_atNy8TpEKW-Gv0-vU20BzcO6MN_gQFibDWOtUb3SCGpmjQpoYYpvru_TsIA-V_io";
        String clientSecret = "EIVHw-0paOwS1TAXrUyF8EU1VWH1ROvNIN4f6orXJZn4NNtRBCagQsokw1Mx8wsyzwR2dewdHTDEyWkR";
        System.err.println("test");
        OAuthTokenCredential tokenCredential = Payment.initConfig(new File("../sdk_config.properties"));
        System.err.println("test");
//        OAuthTokenCredential tokenCredential
//                = new OAuthTokenCredential("AWvE0BAwWOfvkR-_atNy8TpEKW-Gv0-vU20BzcO6MN_gQFibDWOtUb3SCGpmjQpoYYpvru_TsIA-V_io", "EIVHw-0paOwS1TAXrUyF8EU1VWH1ROvNIN4f6orXJZn4NNtRBCagQsokw1Mx8wsyzwR2dewdHTDEyWkR");
        System.err.println("test1");
//        String accessToken = tokenCredential.getAccessToken();
        String accessToken = new OAuthTokenCredential(clientID, clientSecret).getAccessToken();

//APIContext apiContext = new APIContext(accessToken, requestId);
//Payment payment = new Payment();
//payment.setIntent("sale");
        System.err.println("test1");
        Address billingAddress = new Address();
        billingAddress.setLine1("52 N Main ST");
        billingAddress.setCity("Johnstown");
        billingAddress.setCountryCode("US");
        billingAddress.setPostalCode("43210");
        billingAddress.setState("OH");
        System.err.println("test2");

        CreditCard creditCard = new CreditCard();
        creditCard.setNumber("4417119669820331");
        creditCard.setType("visa");
        creditCard.setExpireMonth(11);
        creditCard.setExpireYear(2018);
        creditCard.setCvv2(123);
        creditCard.setFirstName("Joe");
        creditCard.setLastName("Shopper");
        creditCard.setBillingAddress(billingAddress);
        System.err.println("test3");

        Details details = new Details();
        details.setSubtotal("7.41");
        details.setTax("0.03");
        details.setShipping("0.03");
        System.err.println("test4");

        Amount amount = new Amount();
        amount.setTotal("7.47");
        amount.setCurrency("USD");
        amount.setDetails(details);
        System.err.println("test5");

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("This is the payment transaction description.");
        System.err.println("test6");

        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);
        System.err.println("test7");

        FundingInstrument fundingInstrument = new FundingInstrument();
        fundingInstrument.setCreditCard(creditCard);
        System.err.println("test8");

        List<FundingInstrument> fundingInstruments = new ArrayList<FundingInstrument>();
        fundingInstruments.add(fundingInstrument);
        System.err.println("test9");

        Payer payer = new Payer();
        payer.setFundingInstruments(fundingInstruments);
        payer.setPaymentMethod("credit_card");
        System.err.println("test10");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        System.err.println("test");

        Payment createdPayment = payment.create(accessToken);
        
        System.err.println("payment created: " + createdPayment);
    }
}
