package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.model.IncomingAnswers;

import java.net.URISyntaxException;

public interface AnswerService {
    String answerQuestions(IncomingAnswers incomingAnswers) throws URISyntaxException;
}
