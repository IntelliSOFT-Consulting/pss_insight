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
    @PostMapping("createSurvey")
    public RespondentQuestions createSurvey(@RequestBody RespondentIndicators respondentIndicators) throws MessagingException {
        return respondentService.sendRespondentQuestions(respondentIndicators);
    }
    @PostMapping("verifyRespondent")
    public String verifyRespondent(@RequestBody VerifyRespondent verifyRespondent) {
        return respondentService.verifyRespondent(verifyRespondent);
    }
    @GetMapping("showQuestions")
    public List<IndicatorForFrontEnd> showQuestions(@RequestParam Long surveyId) {
        return respondentService.sendVerifiedQuestions(surveyId);
    }

}
