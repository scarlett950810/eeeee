/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.distribution;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.paypal.api.payments.Address;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import com.paypal.api.payments.*;
import com.paypal.base.Constants;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalResource;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import com.sun.faces.facelets.util.Path;
import static com.sun.faces.facelets.util.Path.context;
import java.io.IOException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.slf4j.LoggerFactory;
//import org.slf4j.impl.SimpleLogger;
//import org.slf4j.impl.SimpleLoggerFactory;

/**
 *
 * @author ruicai
 */
@Named(value = "distributionRestPayment")
@ViewScoped
public class DistributionRestPayment implements Serializable {

    private String name;
    private String price;

    /**
     * Creates a new instance of DistributionRestPayment
     */
    public DistributionRestPayment() {
    }

    /**
     *
     * @throws PayPalRESTException
     */
    public void restPay() throws PayPalRESTException, IOException {

        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.DEBUG);

        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.DEBUG);

        String clientID = "AWvE0BAwWOfvkR-_atNy8TpEKW-Gv0-vU20BzcO6MN_gQFibDWOtUb3SCGpmjQpoYYpvru_TsIA-V_io";
        String clientSecret = "EIVHw-0paOwS1TAXrUyF8EU1VWH1ROvNIN4f6orXJZn4NNtRBCagQsokw1Mx8wsyzwR2dewdHTDEyWkR";
        System.err.println("test");

        OAuthTokenCredential tokenCredential = Payment.initConfig(new File("sdk_config.properties"));
        System.err.println("test");
//        OAuthTokenCredential tokenCredential
//                = new OAuthTokenCredential("AWvE0BAwWOfvkR-_atNy8TpEKW-Gv0-vU20BzcO6MN_gQFibDWOtUb3SCGpmjQpoYYpvru_TsIA-V_io", "EIVHw-0paOwS1TAXrUyF8EU1VWH1ROvNIN4f6orXJZn4NNtRBCagQsokw1Mx8wsyzwR2dewdHTDEyWkR");
        System.err.println("test1");
        String accessToken = tokenCredential.getAccessToken();
      //  String accessToken = new OAuthTokenCredential(clientID, clientSecret).getAccessToken();

//APIContext apiContext = new APIContext(accessToken, requestId);
//Payment payment = new Payment();
//payment.setIntent("sale");
        System.err.println("test1");
//        Address billingAddress = new Address();
//        
//        billingAddress.setLine1("52 N Main ST");
//        billingAddress.setCity("Johnstown");
//        billingAddress.setCountryCode("US");
//        billingAddress.setPostalCode("43210");
//        billingAddress.setState("OH");
        System.err.println("test2");
        Item item = new Item();
        item.setName("AIRLINE_TICKET");
        item.setPrice("5000000");
        item.setQuantity("1");
        item.setCurrency("SGD");
        
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<Item>();
        items.add(item);
        itemList.setItems(items);
//        CreditCard creditCard = new CreditCard();
//        creditCard.setNumber("4417119669820331");
//        creditCard.setType("visa");
//        creditCard.setExpireMonth(11);
//        creditCard.setExpireYear(2018);
//        creditCard.setCvv2(123);
//        creditCard.setFirstName("Joe");
//        creditCard.setLastName("Shopper");
//        creditCard.setBillingAddress(billingAddress);
        System.err.println("test3");

//        Details details = new Details();
//        details.setSubtotal("7.41");
//        details.setTax("0.03");
//        details.setShipping("0.03");
        System.err.println("test4");

        Amount amount = new Amount();
        
//        amount.setDetails(details);
        System.err.println("test5");
        amount.setCurrency(item.getCurrency());
        amount.setTotal(item.getPrice());
        
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setItemList(itemList);
        transaction.setDescription("This is the payment transaction description.");
        System.err.println("test6");

        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);
        System.err.println("test7");

//        FundingInstrument fundingInstrument = new FundingInstrument();
//       fundingInstrument.setCreditCard(creditCard);
//        System.err.println("test8");
//        List<FundingInstrument> fundingInstruments = new ArrayList<FundingInstrument>();
//        fundingInstruments.add(fundingInstrument);
//        System.err.println("test9");

        Payer payer = new Payer();
//        payer.setFundingInstruments(fundingInstruments);
        payer.setPaymentMethod("paypal");
        System.err.println("test10");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        System.err.println("test");
        RedirectUrls urls = new RedirectUrls();
        urls.setReturnUrl("https://wwww.baidu.com");
        urls.setCancelUrl("https://www.google.com.sg");
        payment.setRedirectUrls(urls);
        
//        Address billingAddress = new Address();
//        billingAddress.setLine1("52 N Main ST");
//        billingAddress.setCity("Johnstown");
//        billingAddress.setCountryCode("US");
//        billingAddress.setPostalCode("43210");
//        billingAddress.setState("OH");
//
//        CreditCard creditCard = new CreditCard();
//        creditCard.setNumber("4417119669820331");
//        creditCard.setType("visa");
//        creditCard.setExpireMonth(11);
//        creditCard.setExpireYear(2018);
//        creditCard.setCvv2(874);
//        creditCard.setFirstName("Joe");
//        creditCard.setLastName("Shopper");
//        creditCard.setBillingAddress(billingAddress);
//
//        Details amountDetails = new Details();
//        amountDetails.setTax("0.03");
//        amountDetails.setShipping("0.03");
//
//        Amount amount = new Amount();
//        amount.setTotal("7.47");
//        amount.setCurrency("USD");
//        amount.setDetails(amountDetails);
//
//        Transaction transaction = new Transaction();
//        transaction.setAmount(amount);
//        transaction.setDescription("This is the payment transaction description.");
//
//        List<Transaction> transactions = new ArrayList<Transaction>();
//        transactions.add(transaction);
//
//        FundingInstrument fundingInstrument = new FundingInstrument();
//        fundingInstrument.setCreditCard(creditCard);
//
//        List<FundingInstrument> fundingInstruments = new ArrayList<FundingInstrument>();
//        fundingInstruments.add(fundingInstrument);
//
//        Payer payer = new Payer();
//        payer.setFundingInstruments(fundingInstruments);
//        payer.setPaymentMethod("credit_card");
//
//        Payment payment = new Payment();
//        payment.setIntent("sale");
//        payment.setPayer(payer);
//        payment.setTransactions(transactions);

        Payment createdPayment = payment.create(accessToken);
        System.err.println("test12345");
        List<Links> approvalLink = createdPayment.getLinks();
        
        Links link = approvalLink.get(1);
        System.err.println("getHref:"+link.getHref());
        
        
//        System.err.println("payment created: " + createdPayment);
//        Map<String, String> sdkConfig = new HashMap<>();
//// Init config
//        sdkConfig.put(Constants.MODE, Constants.SANDBOX);
//        sdkConfig.put(Constants.ENDPOINT, Constants.REST_SANDBOX_ENDPOINT);
//
//// Create API context
//        String token = new OAuthTokenCredential(clientID, clientSecret, sdkConfig).getAccessToken();
//        context = new APIContext(token);
//
//// Set configuration map
//        Properties props = new Properties();
//        props.putAll(sdkConfig);
//        PayPalResource.initConfig(props);
//        context.setConfigurationMap(sdkConfig);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
