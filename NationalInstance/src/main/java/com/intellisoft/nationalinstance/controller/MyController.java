package com.intellisoft.nationalinstance.controller;


import com.intellisoft.nationalinstance.db.RespondentQuestions;
import com.intellisoft.nationalinstance.model.*;
import com.intellisoft.nationalinstance.service_impl.AnswerService;
import com.intellisoft.nationalinstance.service_impl.RespondentService;
import com.intellisoft.nationalinstance.service_impl.VersionService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

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


//    @GetMapping("/programs1")
//    public List<IndicatorForFrontEnd> getIndicatorForFrontEnd() throws URISyntaxException {
//        return versionService.getIndicators();
//    }

    @PostMapping("answer1")
    public String answer(@RequestBody IncomingAnswers incomingAnswers) throws URISyntaxException {
        return answerService.answerQuestions(incomingAnswers);
    }
    /*
    -this endpoint is used to create the respondent questions both for original request and resend request.
    -for resend request you just need to adjust the expiry date to a future one and add request param surveyId e.g. surveyId=1
    -also for rejected questions-resend using this endpoint .add request parameter earlierRejected=true
    -NB for rejected questions do not add surveyId as we are going to create a new survey for these.
     */
    @PostMapping("createOrUpdateSurvey")
    public RespondentQuestions createOrUpdateSurvey(@RequestBody RespondentIndicators respondentIndicators,
                                                    @RequestParam Long surveyId,
                                                    @RequestParam boolean earlierRejected) throws MessagingException {
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

    /*
     This Java code is a method that is used to answer a survey. It takes two parameters, an object containing the answers to
     the survey and the survey ID, and returns an object containing the questions from the survey. It throws an exception if
     the URI syntax is incorrect.
    */
     @PostMapping("answerSurvey")
    public RespondentQuestions answerSurvey(@RequestBody IncomingAnswers incomingAnswers,
                                            @RequestParam Long surveyId) throws URISyntaxException {
        return respondentService.answerSurveyQuestions(incomingAnswers, surveyId);
    }

}
