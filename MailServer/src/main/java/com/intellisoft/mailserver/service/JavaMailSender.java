package com.intellisoft.mailserver.service;

import com.intellisoft.mailserver.DbRespondents;
import com.intellisoft.mailserver.DbSurveyRespondent;
import com.intellisoft.mailserver.Results;

import java.util.List;

public interface JavaMailSender {

    Results sendMail(DbRespondents dbRespondents);
}
