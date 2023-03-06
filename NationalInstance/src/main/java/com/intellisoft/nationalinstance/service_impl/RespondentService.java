package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.db.Indicators;
import com.intellisoft.nationalinstance.db.RespondentQuestions;
import com.intellisoft.nationalinstance.model.*;

import javax.mail.MessagingException;
import java.net.URISyntaxException;
import java.util.List;

public interface RespondentService {
    RespondentQuestions sendRespondentQuestions(RespondentIndicators respondentIndicators, Long surveyId, boolean earlierRejected) throws MessagingException;
    String verifyRespondent(VerifyRespondent verifyRespondent);
    SurveyQuestions sendVerifiedQuestions(Long sureveyId);
    RespondentQuestions answerSurveyQuestions(IncomingAnswers incomingAnswers , Long sureveyId) throws URISyntaxException;
}
