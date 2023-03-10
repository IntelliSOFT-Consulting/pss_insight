package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.db.RespondentQuestions;
import com.intellisoft.nationalinstance.db.repso.IndicatorsRepo;
import com.intellisoft.nationalinstance.db.repso.RespondentQuestionsRepo;
import com.intellisoft.nationalinstance.exception.CustomException;
import com.intellisoft.nationalinstance.model.*;
import com.intellisoft.nationalinstance.util.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class RespondentServiceImpl implements RespondentService {
    private  final IndicatorsRepo indicatorsRepo;
    private final RespondentQuestionsRepo respondentQuestionsRepo;
    private final EmailService emailService;
    private final VersionService versionService;
    private final AnswerService answerService;
    @Override
    public RespondentQuestions sendRespondentQuestions(RespondentIndicators respondentIndicators,Long surveyId, boolean earlierRejected) throws MessagingException {
        String otp = UUID.randomUUID().toString().split("-")[0];
        RespondentQuestions obj = new RespondentQuestions();
        if (surveyId!= null) {
            var s =respondentQuestionsRepo.findById(surveyId);
            if (s.isPresent()) {
                obj = s.get();
            }else throw new CustomException("Survey not found for id: "+surveyId);
        }
        else
         obj= RespondentQuestions.builder().otp(otp).expiryDate(respondentIndicators.getExpiryDateTime())
                .indicators(respondentIndicators.getIndicators()).build();
        log.info(obj.toString());
        var saved =respondentQuestionsRepo.save(obj);
        log.info("SAVED:{}",saved.toString());
        String htmlMsg = "<body style='border:2px solid black'>"
                +"Hello User, \n Please Click on this link to continue to the survey"+respondentIndicators.getVerificationUrl()+"</body>";
        emailService.sendEmail(respondentIndicators.getEmailAddress(),"QUESTIONS",htmlMsg);
    return obj;
    }

    @Override
    public String verifyRespondent(VerifyRespondent verifyRespondent) {
        var res = respondentQuestionsRepo.findById(verifyRespondent.getSurveyId());
        if (res.isPresent() && (Objects.equals(res.get().getOtp(),verifyRespondent.getOtp()))) {
            res.get().setVerified(true);
            respondentQuestionsRepo.save(res.get());
            if (res.get().getExpiryDate().isAfter(LocalDateTime.now()))
                return "verified";
            else return "Survey link is expired.Please request a new one";
        }
        return "OTP verification failed";
    }

    @Override
    public SurveyQuestions sendVerifiedQuestions(Long surveyId) {
        SurveyQuestions survey = new SurveyQuestions();
        var respondentQuestions = respondentQuestionsRepo.findById(surveyId);
        List<IndicatorForFrontEnd> responses = new ArrayList<>();
        if (respondentQuestions.isPresent()) {
            if (respondentQuestions.get().getExpiryDate().isAfter(LocalDateTime.now()) && respondentQuestions.get().isVerified()) {
                var quetionIds = respondentQuestions.get().getIndicators();
                var forFrontEnd = indicatorsRepo.findIndicatorByIndicatorIds(quetionIds);
                responses = versionService.extractIndicators(forFrontEnd);
                survey.setSurveyId(surveyId);
                survey.setIndicatorForFrontEnds(responses);
            }
            else throw new CustomException("Survey link is expired.Please request a new one");


        }
        return survey;
    }

    @Override
    public RespondentQuestions answerSurveyQuestions(IncomingAnswers incomingAnswers, Long surveyId) throws URISyntaxException {
        var res = respondentQuestionsRepo.findById(surveyId);
        if (res.isPresent())
        {
            res.get().setComments(incomingAnswers.getComments());
            answerService.answerQuestions(incomingAnswers, surveyId);
            return respondentQuestionsRepo.save(res.get());
        }

        throw new CustomException("Unable to add answers.Please try again");
    }

}
