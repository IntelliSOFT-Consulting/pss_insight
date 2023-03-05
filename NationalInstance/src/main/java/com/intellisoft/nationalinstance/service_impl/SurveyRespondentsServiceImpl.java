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
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveyRespondentsServiceImpl implements SurveyRespondentsService{

    private final SurveyRespondentsRepo respondentsRepo;

    private final FormatterClass formatterClass = new FormatterClass();
    private final EmailService emailService;

    private final SurveysService surveysService;
    private final IndicatorsRepo indicatorsRepo;
    private final RespondentAnswersRepo respondentAnswersRepo;
    private final SurveyResendRequestsRepo surveyResendRequestsRepo;


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

        try{
            String password = formatterClass.getOtp();
            String htmlMsg = "<body style='border:2px solid black'>"
                    +"Hello User, \n Please Click on this link to continue to the survey"+
                    customAppUrl +
                    "Your Password is: " + password +
                    "</body>";

            SurveyRespondents surveyRespondents = new SurveyRespondents();
            surveyRespondents.setSurveyId(surveyId);
            surveyRespondents.setEmailAddress(emailAddress);
            surveyRespondents.setPassword(password);
            surveyRespondents.setStatus(PublishStatus.SENT.name());
            surveyRespondents.setExpiryTime(expiryDateTime);
            surveyRespondents.setCustomUrl(customAppUrl);

            respondentsRepo.save(surveyRespondents);

            emailService.sendEmail(emailAddress,"QUESTIONS",htmlMsg);
        }catch (MessagingException e ){
            e.printStackTrace();
        }



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
            Long surveyId = Long.valueOf(surveyRespondents.getSurveyId());
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
        Object answer = dbResponse.getIndicator().getAnswer();
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

            /**
             * Check expiry time
             */
            Triple<Long, Long, Long> tripleData = formatterClass.getRemainingTime(expiryDateTime);
            Long days = tripleData.getFirst();
            if (days > 1){
                try{
                    String password = formatterClass.getOtp();
                    String htmlMsg = "<body style='border:2px solid black'>"
                            +"Hello User, \n Please Click on this link to continue to the survey"+
                            customAppUrl +
                            "Your Password is: " + password +
                            "</body>";

                    surveyRespondents.setEmailAddress(emailAddress);
                    surveyRespondents.setPassword(password);
                    surveyRespondents.setStatus(PublishStatus.ACCEPTED.name());
                    surveyRespondents.setExpiryTime(expiryDateTime);
                    surveyRespondents.setCustomUrl(customAppUrl);

                    respondentsRepo.save(surveyRespondents);

                    emailService.sendEmail(emailAddress,"QUESTIONS",htmlMsg);

                    return new Results(200, new DbDetails("Email sent."));
                }catch (MessagingException e ){
                    e.printStackTrace();
                }
            }else {
                return new Results(400,"The expiry date has reached.");
            }

        }

        return new Results(400, "The email could not be sent.");
    }
}
