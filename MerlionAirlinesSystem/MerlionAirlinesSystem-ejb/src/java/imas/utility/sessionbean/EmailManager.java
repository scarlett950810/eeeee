/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.utility.sessionbean;

import java.util.Date;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailManager {

    public EmailManager() {
    }

    public static boolean run(String addressedTo, String subject, String content) {

        //String toAddress = "winexpress.noreply@gmail.com"; // set the email address to send to
        String recipients = addressedTo.trim(); // trim the recipients address to avoid space
        //String contentType = "text/plain"; // set content type -- only plain text
        String fromAddress = "lihao0426@gmail.com";
        String smtpHost = "smtp.gmail.com"; // set the mailbox to send from 
        int smtpPort = 587; // set port
        String username = "lihao0426@gmail.com"; // mailbox address
        String password = "howe0819!"; // *password*

        try {
            //Address recipient = new InternetAddress(addressedTo.trim());
            //Address[] recipients = {recipient};
            Properties props = System.getProperties(); //
            props.put("mail.smtp.starttls.enable", "true");
//            Session session = Session.getDefaultInstance(props);
            Session session = Session.getInstance(props, new GMailAuthenticator(username, password));

            MimeMessage message = new MimeMessage(session); // set message type

            //message.setFrom(new InternetAddress(toAddress));
            message.setRecipients(Message.RecipientType.TO, recipients);
            message.setFrom(new InternetAddress(fromAddress));
            //message.setRecipients(Message.RecipientType.TO, toAddress);
            //message.setReplyTo(InternetAddress.parse(recipients, false));
            message.setSubject(subject);

            // This mail has 2 part, the BODY and the embedded image
            MimeMultipart multipart = new MimeMultipart("related");

            // first part (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = content;
            messageBodyPart.setContent(htmlText, "text/html");
            // add it
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
            message.setSentDate(new Date());

            Transport transport = session.getTransport("smtp"); // set protocol
            transport.connect(smtpHost, smtpPort, username, password); // connect mail server
            transport.sendMessage(message, message.getAllRecipients()); // send email
            transport.close(); // close connection

            return true;
        } catch (MessagingException messagingException) {
            System.out.print(messagingException);
            return false;

        } catch (Exception e) {
            System.out.print(e);
            return false;
        }
    }//run

    public static boolean runBookingConfirmation(String addressedTo, String subject, String flight, String passenger, String referenceNumber) {

        //String toAddress = "winexpress.noreply@gmail.com"; // set the email address to send to
        String recipients = addressedTo.trim(); // trim the recipients address to avoid space
        //String contentType = "text/plain"; // set content type -- only plain text
        String fromAddress = "lihao0426@gmail.com";
        String smtpHost = "smtp.gmail.com"; // set the mailbox to send from 
        int smtpPort = 587; // set port
        String username = "lihao0426@gmail.com"; // mailbox address
        String password = "howe0819!"; // *password*

        try {
            //Address recipient = new InternetAddress(addressedTo.trim());
            //Address[] recipients = {recipient};
            Properties props = System.getProperties(); //
            props.put("mail.smtp.starttls.enable", "true");
//            Session session = Session.getDefaultInstance(props);
            Session session = Session.getInstance(props, new GMailAuthenticator(username, password));

            MimeMessage message = new MimeMessage(session); // set message type

            //message.setFrom(new InternetAddress(toAddress));
            message.setRecipients(Message.RecipientType.TO, recipients);
            message.setFrom(new InternetAddress(fromAddress));
            //message.setRecipients(Message.RecipientType.TO, toAddress);
            //message.setReplyTo(InternetAddress.parse(recipients, false));
            message.setSubject(subject);

            // This mail has 2 part, the BODY and the embedded image
            MimeMultipart multipart = new MimeMultipart("related");

            // first part (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = 
                    "<head>"
                    + "<style>"
                    + "table {"
                    + "    width:100%;"
                    + "}"
                    + "table, th, td {"
                    + "    border: 1px solid black;"
                    + "    border-collapse: collapse;"
                    + "}"
                    + "th, td {"
                    + "    padding: 5px;"
                    + "    text-align: left;"
                    + "}"
                    + "table tr:nth-child(even) {"
                    + "    background-color: #eee;"
                    + "}"
                    + "table tr:nth-child(odd) {"
                    + "   background-color:#fff;"
                    + "}"
                    + "table th	{"
                    + "    background-color: black;"
                    + "    color: white;"
                    + "}"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<img src=\"http://i.imgsafe.org/260c51a.png\" width=\"200px\">"
                    + "<h3>Booking Confirmation</h3><br><br><br>"
                    + "<h2>Reference Number: " + referenceNumber
                    + "<table>"
                    + "  <tr>"
                    + "    <th>Flight No</th>"
                    + "    <th>Departure City</th>"
                    + "    <th>Departure Time</th>"
                    + "    <th>Arrival City</th>"
                    + "    <th>Arrival Time</th>"
                    + "  </tr>";
            htmlText = htmlText + flight;
            htmlText = htmlText + "</table>";
            htmlText = htmlText + "<br><br><br>";
            htmlText = htmlText 
                    + "<h3>Passenger List</h3>"
                    + "<table>"
                    + "  <tr>"
                    + "    <th>Name</th>"
                    + "    <th>Passport Number</th>"
                    + "    <th>Nationality</th>"
                    + "  </tr>";
            htmlText = htmlText + passenger + "</table>";
            htmlText = htmlText + "</body>";
            
            messageBodyPart.setContent(htmlText, "text/html");
            // add it
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
            message.setSentDate(new Date());

            Transport transport = session.getTransport("smtp"); // set protocol
            transport.connect(smtpHost, smtpPort, username, password); // connect mail server
            transport.sendMessage(message, message.getAllRecipients()); // send email
            transport.close(); // close connection

            return true;
        } catch (MessagingException messagingException) {
            System.out.print(messagingException);
            return false;

        } catch (Exception e) {
            System.out.print(e);
            return false;
        }
    }//run
    
    public static void runMemberActivationEmail(String addressedTo, String name, String activationLink, String memberID){
        
        //String toAddress = "winexpress.noreply@gmail.com"; // set the email address to send to
        String recipients = addressedTo.trim(); // trim the recipients address to avoid space
        //String contentType = "text/plain"; // set content type -- only plain text
        String fromAddress = "lihao0426@gmail.com";
        String smtpHost = "smtp.gmail.com"; // set the mailbox to send from 
        int smtpPort = 587; // set port
        String username = "lihao0426@gmail.com"; // mailbox address
        String password = "howe0819!"; // *password*

        try {
            //Address recipient = new InternetAddress(addressedTo.trim());
            //Address[] recipients = {recipient};
            Properties props = System.getProperties(); //
            props.put("mail.smtp.starttls.enable", "true");
//            Session session = Session.getDefaultInstance(props);
            Session session = Session.getInstance(props, new GMailAuthenticator(username, password));

            MimeMessage message = new MimeMessage(session); // set message type

            //message.setFrom(new InternetAddress(toAddress));
            message.setRecipients(Message.RecipientType.TO, recipients);
            message.setFrom(new InternetAddress(fromAddress));
            //message.setRecipients(Message.RecipientType.TO, toAddress);
            //message.setReplyTo(InternetAddress.parse(recipients, false));
            message.setSubject("Welcome to MerFlion | Fly more with Merlion and get more benefits");

            // This mail has 2 part, the BODY and the embedded image
            MimeMultipart multipart = new MimeMultipart("related");

            // first part (the html)
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<body>" + "<img src=\"http://i.imgsafe.org/260c51a.png\" width=\"200px\" /> "
                    + "<h1 style=\"float: right\">Welcome to the Merflion Member Club</h1><br><br><br>"
                    + "<p>Dear " + name + "</p>" + "<p>A very warm welcome to you for being part of this family "
                    + "and we look forward to serving you to your best satisfaction.<br> We have over 170 "
                    + "choices of destination all over the world and we have the newest and safeest aircrafts "
                    + "in the world. With you being a member of Merflion Member Club, you can accuumalate "
                    + "mileages every time you fly with Merflion Airlines and enjoy exclusive benefits.</p>" 
                    + "<br>" + "<p>Your member ID: " + memberID + "</p><br><br><p>Please click the activation below to activate your account:</p>" + "<br>"
                    + activationLink + "<br>" + "<h3>Your sincerely</h3>" + "<h4>Merlion Airlines</h4>" + "</body>";

            messageBodyPart.setContent(htmlText, "text/html");
            // add it
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);
            message.setSentDate(new Date());

            Transport transport = session.getTransport("smtp"); // set protocol
            transport.connect(smtpHost, smtpPort, username, password); // connect mail server
            transport.sendMessage(message, message.getAllRecipients()); // send email
            transport.close(); // close connection
        } catch (MessagingException messagingException) {
            System.out.print(messagingException);

        } catch (Exception e) {
            System.out.print(e);
        }
    }//run
    

    public static void runMarketingEmail(String addressedTo, String subject, String content) {

        //String toAddress = "winexpress.noreply@gmail.com"; // set the email address to send to
        String recipients = addressedTo.trim(); // trim the recipients address to avoid space
        //String contentType = "text/plain"; // set content type -- only plain text
        String fromAddress = "lihao0426@gmail.com";
        String smtpHost = "smtp.gmail.com"; // set the mailbox to send from 
        int smtpPort = 587; // set port
        String username = "lihao0426@gmail.com"; // mailbox address
        String password = "howe0819!"; // *password*

        try {
            Properties props = System.getProperties(); //
            props.put("mail.smtp.starttls.enable", "true");
//            Session session = Session.getDefaultInstance(props);
            Session session = Session.getInstance(props, new GMailAuthenticator(username, password));

            MimeMessage message = new MimeMessage(session); // set message type

            //message.setFrom(new InternetAddress(toAddress));
            message.setRecipients(Message.RecipientType.TO, recipients);
            message.setFrom(new InternetAddress(fromAddress));
            //message.setRecipients(Message.RecipientType.TO, toAddress);
            //message.setReplyTo(InternetAddress.parse(recipients, false));
            message.setSubject(subject);

            // This mail has 2 part, the BODY and the embedded image
            MimeMultipart multipart = new MimeMultipart("related");

            // ContentID is used by both parts
            String cid = ContentIdGenerator.getContentId();

            // HTML part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("<html><head>"
                    + "<title>This is not usually displayed</title>"
                    + "</head>\n"
                    + "<body><div><b>Hi there!</b></div>"
                    + "<div>Sending HTML in email is so <i>cool!</i> </div>\n"
                    + "<div>And here's an image: <img src=\"cid:"
                    + cid
                    + "\" /></div>\n" + "<div>I hope you like it!</div></body></html>",
                    "US-ASCII", "html");
            multipart.addBodyPart(textPart);

            // Image part
            MimeBodyPart imagePart = new MimeBodyPart();
            imagePart.attachFile("../resources/img/NEW_LOGO.png");
            imagePart.setContentID("<" + cid + ">");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);
            message.setSentDate(new Date());

            Transport transport = session.getTransport("smtp"); // set protocol
            transport.connect(smtpHost, smtpPort, username, password); // connect mail server
            transport.sendMessage(message, message.getAllRecipients()); // send email
            transport.close(); // close connection

        } catch (MessagingException messagingException) {
            System.out.print(messagingException);

        } catch (Exception e) {
            System.out.print(e);
        }
    }//run
}
