package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.db.RespondentQuestions;
import com.intellisoft.nationalinstance.db.repso.IndicatorsRepo;
import com.intellisoft.nationalinstance.db.repso.RespondentQuestionsRepo;
import com.intellisoft.nationalinstance.model.IndicatorForFrontEnd;
import com.intellisoft.nationalinstance.model.RespondentIndicators;
import com.intellisoft.nationalinstance.model.VerifyRespondent;
import com.intellisoft.nationalinstance.util.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
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
    @Override
    public RespondentQuestions sendRespondentQuestions(RespondentIndicators respondentIndicators) throws MessagingException {
//        List<String> metaData = indicatorsRepo.findByIndicatorIds(respondentIndicators.getIndicators());
        String otp = UUID.randomUUID().toString().split("-")[0];
        var obj= RespondentQuestions.builder().otp(otp).expiryDate(LocalDateTime.now().plusMinutes(1L))
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
        if (res.isPresent() && (Objects.equals(res.get().getOtp(),verifyRespondent.getOtp())))
                return "verified";
        return "verification failed";
    }

    @Override
    public List<IndicatorForFrontEnd> sendVerifiedQuestions(Long surveyId) {
        var respondentQuestions = respondentQuestionsRepo.findById(surveyId);
        List<IndicatorForFrontEnd> responses = new ArrayList<>();
        if (respondentQuestions.isPresent()) {
            var quetionIds = respondentQuestions.get().getIndicators();
            var forFrontEnd = indicatorsRepo.findIndicatorByIndicatorIds(quetionIds);
             responses = versionService.extractIndicators(forFrontEnd);

        }
        return responses;
    }

}
