package com.intellisoft.nationalinstance.controller;


import com.intellisoft.nationalinstance.db.RespondentQuestions;
import com.intellisoft.nationalinstance.db.VersionEntity;
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

    @Operation(
            summary = "Download a File",
            description = "Download a file to the server's file system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.response-codes.ok.desc}"),
            @ApiResponse(responseCode = "400", description = "${api.response-codes.badRequest.desc}",
                    content = { @Content(examples = { @ExampleObject(value = "") }) }),
            @ApiResponse(responseCode = "404", description = "${api.response-codes.notFound.desc}",
                    content = { @Content(examples = { @ExampleObject(value = "") }) }) })
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
    public RespondentQuestions answerSurvey(@RequestBody IncomingAnswers incomingAnswers, @RequestParam Long surveyId) throws URISyntaxException {
        return respondentService.answerSurveyQuestions(incomingAnswers, surveyId);
    }

}
