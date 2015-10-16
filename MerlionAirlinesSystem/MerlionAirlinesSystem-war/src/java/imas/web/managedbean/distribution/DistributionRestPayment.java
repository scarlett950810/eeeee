///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package imas.web.managedbean.distribution;
//
//import com.paypal.api.payments.Address;
//import com.paypal.api.payments.Amount;
//import com.paypal.api.payments.CreditCard;
//import com.paypal.api.payments.Details;
//import com.paypal.api.payments.FundingInstrument;
//import com.paypal.api.payments.Payer;
//import com.paypal.api.payments.Payment;
//import com.paypal.api.payments.Transaction;
//import com.paypal.base.rest.OAuthTokenCredential;
//import com.paypal.base.rest.PayPalRESTException;
//import java.util.ArrayList;
//import java.util.List;
//import javax.annotation.PostConstruct;
//import javax.inject.Named;
//import javax.faces.view.ViewScoped;
//
///**
// *
// * @author ruicai
// */
//@Named(value = "distributionRestPayment")
//@ViewScoped
//public class DistributionRestPayment {
//    
//    /**
//     * Creates a new instance of DistributionRestPayment
//     */
//    public DistributionRestPayment() {
//    }
//    
//    
//    @PostConstruct
//    public void init() throws PayPalRESTException {
//    
//        OAuthTokenCredential tokenCredential =
//  new OAuthTokenCredential("<CLIENT_ID>", "<CLIENT_SECRET>");
//  
//String accessToken = tokenCredential.getAccessToken();
//
//Address billingAddress = new Address();
//billingAddress.setLine1("52 N Main ST");
//billingAddress.setCity("Johnstown");
//billingAddress.setCountryCode("US");
//billingAddress.setPostalCode("43210");
//billingAddress.setState("OH");
//
//CreditCard creditCard = new CreditCard();
//creditCard.setNumber("4417119669820331");
//creditCard.setType("visa");
//creditCard.setExpireMonth(11);
//creditCard.setExpireYear(2018);
//creditCard.setCvv2(123);
//creditCard.setFirstName("Joe");
//creditCard.setLastName("Shopper");
//creditCard.setBillingAddress(billingAddress);
//
//Details details = new Details();
//details.setSubtotal("7.41");
//details.setTax("0.03");
//details.setShipping("0.03");
//
//Amount amount = new Amount();
//amount.setTotal("7.47");
//amount.setCurrency("USD");
//amount.setDetails(details);
//		
//Transaction transaction = new Transaction();
//transaction.setAmount(amount);
//transaction.setDescription("This is the payment transaction description.");
//
//List<Transaction> transactions = new ArrayList<Transaction>();
//transactions.add(transaction);
//
//FundingInstrument fundingInstrument = new FundingInstrument();
//fundingInstrument.setCreditCard(creditCard);
//
//List<FundingInstrument> fundingInstruments = new ArrayList<FundingInstrument>();
//fundingInstruments.add(fundingInstrument);
//
//Payer payer = new Payer();
//payer.setFundingInstruments(fundingInstruments);
//payer.setPaymentMethod("credit_card");
//
//Payment payment = new Payment();
//payment.setIntent("sale");
//payment.setPayer(payer);
//payment.setTransactions(transactions);
//
//Payment createdPayment = payment.create(accessToken);
//
//        System.err.println("payment created: "+ createdPayment);
//    }
//}
