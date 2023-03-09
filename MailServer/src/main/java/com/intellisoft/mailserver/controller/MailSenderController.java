package com.intellisoft.mailserver.controller;

import com.intellisoft.mailserver.DbRespondents;
import com.intellisoft.mailserver.DbSurveyRespondent;
import com.intellisoft.mailserver.FormatterClass;
import com.intellisoft.mailserver.Results;
import com.intellisoft.mailserver.service.JavaMailSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping(value = "/api/v1/mail-service")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class MailSenderController {

    private final JavaMailSender javaMailSender;
    private final FormatterClass formatterClass = new FormatterClass();

    public MailSenderController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(
            @RequestBody DbRespondents dbRespondents) {
        Results results = javaMailSender.sendMail(dbRespondents);
        return formatterClass.getResponse(results);
    }
}
