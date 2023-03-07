package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.DbSurvey;
import com.intellisoft.nationalinstance.Results;

public interface SurveysService {

    Results addSurvey(DbSurvey dbSurvey);
    Results listAdminSurveys(String creatorId);
    Results listRespondentsSurveys(String creatorId);
    Results surveyDetails(String id);
    Results deleteSurvey(Long id);
    Results updateSurvey(Long id, DbSurvey dbSurvey);

}
