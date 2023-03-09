package com.intellisoft.mailserver.service;

import com.intellisoft.mailserver.*;
import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class JavaMailSenderImpl implements JavaMailSender{

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApi;

    private final FormatterClass formatterClass = new FormatterClass();

    @Async
    void sendEmailBackground(List<DbSurveyRespondent> surveyRespondentList) throws IOException {

        System.out.println(sendGridApi);

        Email from = new Email("pssnotifications23@gmail.com");
        String subject = "PSS Survey";
        SendGrid sg = new SendGrid(sendGridApi);
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
