package com.intellisoft.nationalinstance.util;


import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class MailService {

    public void sendTextEmail() throws IOException {
        // the sender email should be the same as we used to Create a Single Sender Verification
//        Email from = new Email("dnjau@intellisoftkenya.com");
//        String subject = "Sending with SendGrid is Fun";
//        Email to = new Email("davidnjau21@gmail.com");
//        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
//        Mail mail = new Mail(from, subject, to, content);
//
//        SendGrid sg = new SendGrid("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");
//        Request request = new Request();
//        try {
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//            request.setBody(mail.build());
//            Response response = sg.api(request);
//            System.out.println(response.getStatusCode());
//            System.out.println(response.getBody());
//            System.out.println(response.getHeaders());
//        } catch (IOException ex) {
//            throw ex;
//        }
    }
}