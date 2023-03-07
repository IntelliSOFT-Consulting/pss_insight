package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.*;
import com.intellisoft.nationalinstance.db.RespondentAnswers;
import com.intellisoft.nationalinstance.db.SurveyResendRequests;
import com.intellisoft.nationalinstance.db.SurveyRespondents;
import com.intellisoft.nationalinstance.db.Surveys;
import com.intellisoft.nationalinstance.db.repso.IndicatorsRepo;
import com.intellisoft.nationalinstance.db.repso.RespondentAnswersRepo;
import com.intellisoft.nationalinstance.db.repso.SurveyResendRequestsRepo;
import com.intellisoft.nationalinstance.db.repso.SurveyRespondentsRepo;
import com.intellisoft.nationalinstance.util.EmailService;
import kotlin.Triple;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveyRespondentsServiceImpl implements SurveyRespondentsService{

    private final SurveyRespondentsRepo respondentsRepo;

    private final FormatterClass formatterClass = new FormatterClass();
    private final SurveysService surveysService;
    private final IndicatorsRepo indicatorsRepo;
    private final RespondentAnswersRepo respondentAnswersRepo;
    private final SurveyResendRequestsRepo surveyResendRequestsRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public TemplateEngine templateEngine;

    private final EmailService emailService;


    @Override
    public Results addSurveyRespondent(DbSurveyRespondent dbSurveyRespondent) {

        List<String> emailAddressList = dbSurveyRespondent.getEmailAddressList();
        String expiryDateTime = dbSurveyRespondent.getExpiryDateTime();
        String surveyId = dbSurveyRespondent.getSurveyId();
        String customAppUrl = dbSurveyRespondent.getCustomAppUrl();

        for (int i = 0; i < emailAddressList.size(); i++){

            String emailAddress = emailAddressList.get(i);
            sendMail(emailAddress, expiryDateTime, surveyId, customAppUrl);

        }


        return new Results(200, new DbDetails("Please wait as we send the mails."));
    }

    private void sendMail(String emailAddress,
                          String expiryDateTime,
                          String surveyId,
                          String customAppUrl ){

        String password = formatterClass.getOtp();

        SurveyRespondents surveyRespondents = new SurveyRespondents();
        surveyRespondents.setSurveyId(surveyId);
        surveyRespondents.setEmailAddress(emailAddress);

        surveyRespondents.setStatus(PublishStatus.SENT.name());
        surveyRespondents.setCustomUrl(customAppUrl);

        surveyRespondents.setPassword(password);
        surveyRespondents.setExpiryTime(expiryDateTime);

        Optional<SurveyRespondents> optionalSurveyRespondents =
                respondentsRepo.findByEmailAddressAndSurveyId(emailAddress, surveyId);
        if (optionalSurveyRespondents.isPresent()){
            SurveyRespondents surveyDbRespondents = optionalSurveyRespondents.get();
            surveyDbRespondents.setPassword(password);
            surveyDbRespondents.setExpiryTime(expiryDateTime);
            respondentsRepo.save(surveyDbRespondents);
        }else {
            respondentsRepo.save(surveyRespondents);
        }

        formatterClass.sendMail(
                mailSender,
                templateEngine,
                emailAddress,
                customAppUrl);



    }

    @Override
    public Results listSurveyRespondent(String surveyId) {

        List<SurveyRespondents> respondentsList =
                respondentsRepo.findAllBySurveyId(surveyId);

        DbResults dbResults = new DbResults(
                respondentsList.size(),
                respondentsList);

        return new Results(200, dbResults);
    }

    @Override
    public Results deleteSurveyRespondent(String surveyId) {

        List<SurveyRespondents> respondentsList =
                respondentsRepo.findAllBySurveyId(surveyId);
        if (!respondentsList.isEmpty()){
            respondentsRepo.deleteAllBySurveyId(surveyId);
            return new Results(200,
                    new DbDetails("Records associated with the record have been deleted."));
        }
        return new Results(400, "Could not find any records for that id.");
    }

    @Override
    public Results deleteRespondent(String id) {
        Long respondentId = Long.valueOf(id);
        Optional<SurveyRespondents> optionalSurveyRespondents =
                respondentsRepo.findById(respondentId);
        if (optionalSurveyRespondents.isPresent()){
            respondentsRepo.deleteById(respondentId);
            return new Results(200, new DbDetails("Respondent has been deleted successfully."));
        }
        return new Results(400, "Could not find any records for that id.");
    }

    @Override
    public Results verifyPassword(DbSurveyDetails dbSurveyDetails) {

        Long respondentId = Long.valueOf(dbSurveyDetails.getRespondentId());
        String password = dbSurveyDetails.getPassword();

        Optional<SurveyRespondents> optionalSurveyRespondents =
                respondentsRepo.findById(respondentId);
        if (optionalSurveyRespondents.isPresent()){
            SurveyRespondents surveyRespondents = optionalSurveyRespondents.get();
            String passwordDb = surveyRespondents.getPassword();
            if (password.equals(passwordDb)){
                return new Results(200, new DbDetails("Log in success."));
            }
        }

        return new Results(400, "Password authentication failed.");
    }

    @Override
    public Results getAssignedSurvey(String id) {

        Optional<SurveyRespondents> optionalSurveyRespondents =
                respondentsRepo.findById(Long.valueOf(id));
        if (optionalSurveyRespondents.isPresent()) {
            SurveyRespondents surveyRespondents = optionalSurveyRespondents.get();
            String surveyId = surveyRespondents.getSurveyId();
            Results results = surveysService.surveyDetails(surveyId);
            if (results.getCode() == 200){
                Surveys surveys = (Surveys) results.getDetails();
                if (surveys != null){
                    List<String> stringList = surveys.getIndicators();
                    List<String> metaData = indicatorsRepo.findMetadataByIndicatorIds(stringList);

                    System.out.println("**********");
                    System.out.println(metaData);

                }

            }
        }

        return null;
    }

    @Override
    public Results saveResponse(DbResponse dbResponse) {

        String respondentId = dbResponse.getRespondentId();
        String indicatorId = dbResponse.getIndicator().getIndicatorId();
        String answer = dbResponse.getIndicator().getAnswer();
        String comments = dbResponse.getIndicator().getComments();
        String attachment = dbResponse.getIndicator().getAttachment();

        RespondentAnswers respondentAnswers = new RespondentAnswers(
                respondentId, indicatorId, answer, comments, attachment);
        respondentAnswersRepo.save(respondentAnswers);
        return new Results(201, respondentAnswers);
    }

    @Override
    public Results requestLink(DbRequestLink dbRequestLink) {

        String respondentId = dbRequestLink.getRespondentId();
        String comment = dbRequestLink.getComments();

        SurveyResendRequests surveyResendRequests = new SurveyResendRequests();
        surveyResendRequests.setRespondentId(respondentId);
        surveyResendRequests.setComment(comment);
        surveyResendRequests.setStatus(PublishStatus.REQUESTED.name());

        surveyResendRequestsRepo.save(surveyResendRequests);
        resendEmail(respondentId);

        return new Results(200, new DbDetails("Request has been sent."));
    }

    public Results resendEmail(String respondentId){
        //Get respondent details
        Optional<SurveyRespondents> optionalSurveyRespondents =
                respondentsRepo.findById(Long.valueOf(respondentId));
        if (optionalSurveyRespondents.isPresent()){
            SurveyRespondents surveyRespondents = optionalSurveyRespondents.get();
            String emailAddress = surveyRespondents.getEmailAddress();
            String customAppUrl = surveyRespondents.getCustomUrl();
            String expiryDateTime = surveyRespondents.getExpiryTime();
            String surveyId = surveyRespondents.getSurveyId();

            /**
             * Check expiry time
             */
            Triple<Long, Long, Long> tripleData = formatterClass.getRemainingTime(expiryDateTime);
            Long days = tripleData.getFirst();
            if (days > 1){
                String password = formatterClass.getOtp();

                surveyRespondents.setEmailAddress(emailAddress);
                surveyRespondents.setPassword(password);
                surveyRespondents.setStatus(PublishStatus.ACCEPTED.name());
                surveyRespondents.setExpiryTime(expiryDateTime);
                surveyRespondents.setCustomUrl(customAppUrl);

                respondentsRepo.save(surveyRespondents);
                sendMail(emailAddress, expiryDateTime, surveyId, customAppUrl);

                return new Results(200, new DbDetails("Email sent."));
            }else {
                return new Results(400,"The expiry date has reached.");
            }

        }

        return new Results(400, "The email could not be sent.");
    }
}
