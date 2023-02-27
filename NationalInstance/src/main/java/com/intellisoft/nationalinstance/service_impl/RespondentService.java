package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.db.Indicators;
import com.intellisoft.nationalinstance.db.RespondentQuestions;
import com.intellisoft.nationalinstance.model.IndicatorForFrontEnd;
import com.intellisoft.nationalinstance.model.RespondentIndicators;
import com.intellisoft.nationalinstance.model.VerifyRespondent;

import javax.mail.MessagingException;
import java.util.List;

public interface RespondentService {
    RespondentQuestions sendRespondentQuestions(RespondentIndicators respondentIndicators) throws MessagingException;
    String verifyRespondent(VerifyRespondent verifyRespondent);
    List<IndicatorForFrontEnd> sendVerifiedQuestions(Long sureveyId);
}
