package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.model.IncomingAnswers;
import com.intellisoft.nationalinstance.model.OutgoingAnswers;
import com.intellisoft.nationalinstance.util.AppConstants;
import com.intellisoft.nationalinstance.util.GenericWebclient;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
@Log4j2
public class AnswerServiceImpl implements AnswerService {
    @Override
    public String answerQuestions(IncomingAnswers incomingAnswers) throws URISyntaxException {
        OutgoingAnswers outgoingAnswers=new OutgoingAnswers();
        outgoingAnswers.setStatus(incomingAnswers.getStatus());
        outgoingAnswers.setEventDate(incomingAnswers.getEventDate());
        outgoingAnswers.setDataValues(incomingAnswers.getDataValues());
        log.info("ANSWERS:{}",outgoingAnswers.toString());
        //change to correct national url
        var res = GenericWebclient.postForSingleObjResponse(AppConstants.EVENTS_ENDPOINT,outgoingAnswers, OutgoingAnswers.class,String.class);
        log.info("RES:{}",res);
        return res;
    }
}
