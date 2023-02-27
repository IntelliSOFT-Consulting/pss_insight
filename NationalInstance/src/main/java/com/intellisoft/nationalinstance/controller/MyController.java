package com.intellisoft.nationalinstance.controller;


import com.intellisoft.nationalinstance.db.RespondentQuestions;
import com.intellisoft.nationalinstance.db.VersionEntity;
import com.intellisoft.nationalinstance.model.*;
import com.intellisoft.nationalinstance.service_impl.AnswerService;
import com.intellisoft.nationalinstance.service_impl.RespondentService;
import com.intellisoft.nationalinstance.service_impl.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/v1")
@RestController
@RequiredArgsConstructor
public class MyController {
    private final VersionService versionService;
    private final RespondentService respondentService;
    private final AnswerService answerService;
    @GetMapping("/programs1")
    public List<IndicatorForFrontEnd> getIndicatorForFrontEnd() throws URISyntaxException {
        return versionService.getIndicators();
    }
    @GetMapping("sync1")
    public Response sync() throws URISyntaxException {
        return versionService.syncVersion();
    }
    @PostMapping("/create-version1")
    public VersionEntity createVersion(@RequestBody VersionEntity versionEntity) throws URISyntaxException {
        return versionService.saveDraftOrPublish(versionEntity);
    }
    @PostMapping("answer1")
    public String answer(@RequestBody IncomingAnswers incomingAnswers) throws URISyntaxException {
        return answerService.answerQuestions(incomingAnswers);
    }
    //this endpoint is used to create the respondent questions both for original request and resend request.
    // for resend request you just need to adjust the expiry date to a future one and add request param surveyId eg surveyId=1
    //also for rejected questions- resend using this endpoint .add request parameter earlierRejected=true
    //NB for rejected questions do not add surveyId as we are going to create a new survey for these.
    @PostMapping("createOrUpdateSurvey")
    public RespondentQuestions createOrUpdateSurvey(@RequestBody RespondentIndicators respondentIndicators,@RequestParam Long surveyId,@RequestParam boolean earlierRejected) throws MessagingException {
        return respondentService.sendRespondentQuestions(respondentIndicators,surveyId,earlierRejected);
    }
    @PostMapping("verifyRespondent")
    public String verifyRespondent(@RequestBody VerifyRespondent verifyRespondent) {
        return respondentService.verifyRespondent(verifyRespondent);
    }
    @GetMapping("showSurveyQuestions")
    public SurveyQuestions showQuestions(@RequestParam Long surveyId) {
        return respondentService.sendVerifiedQuestions(surveyId);
    }
    @PostMapping("answerSurvey")
    public RespondentQuestions answerSurvey(@RequestBody IncomingAnswers incomingAnswers, @RequestParam Long surveyId) throws URISyntaxException {
        return respondentService.answerSurveyQuestions(incomingAnswers, surveyId);
    }

}
