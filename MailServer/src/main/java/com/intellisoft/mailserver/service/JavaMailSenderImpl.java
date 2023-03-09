package com.intellisoft.mailserver.service;

import com.intellisoft.mailserver.*;
import com.sendgrid.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class JavaMailSenderImpl implements JavaMailSender{

    private final FormatterClass formatterClass = new FormatterClass();

    @Async
    void sendEmailBackground(List<DbSurveyRespondent> surveyRespondentList) throws IOException {

        Email from = new Email("dnjau@intellisoftkenya.com");
        String subject = "PSS Survey";
        SendGrid sg = new SendGrid("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");
        Request request = new Request();

        // Read the email template from the resources folder
        ClassPathResource classPathResource = new ClassPathResource("templates/email.html");


        for (DbSurveyRespondent dbSurveyRespondent: surveyRespondentList){

            String htmlContent = new String(classPathResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            String emailAddress = dbSurveyRespondent.getEmailAddress();
            String expiryDateTime = dbSurveyRespondent.getExpiryDate();
            String customUrl = dbSurveyRespondent.getCustomUrl();
            String password = dbSurveyRespondent.getPassword();


            // Replace the placeholders in the email template with the dynamic content
            htmlContent = htmlContent.replace("[EMAIL_ADDRESS]", formatterClass.extractName(emailAddress));
            htmlContent = htmlContent.replace("[PASSWORD]", password);
            htmlContent = htmlContent.replace("[ACCESS_LINK]", customUrl);
            htmlContent = htmlContent.replace("[EXPIRY_TIME]", formatterClass.getRemainingTime(expiryDateTime));

            Email to = new Email(emailAddress);
            Content content = new Content("text/html", htmlContent);
            Mail mail = new Mail(from, subject, to, content);
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);

                System.out.println("------");
                System.out.println(response.getStatusCode());
                System.out.println(response.getBody());

            } catch (IOException ex) {
                ex.printStackTrace();
                throw ex;
            }

        }







    }
    @Override
    public Results sendMail(DbRespondents dbRespondents) {

        try{
            List<DbSurveyRespondent> surveyRespondentList = dbRespondents.getRespondents();
            sendEmailBackground(surveyRespondentList);
            return new Results(200, new DbDetails("Email will be sent"));
        }catch (Exception e){
            e.printStackTrace();
            return new Results(400, "There was an issue.");
        }


    }
}
