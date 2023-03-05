package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.DbSurvey;
import com.intellisoft.nationalinstance.Results;

public interface SurveysService {

    Results addSurvey(DbSurvey dbSurvey);
    Results listSurveys(String creatorId);
    Results surveyDetails(Long id);
    Results deleteSurvey(Long id);
    Results updateSurvey(Long id, DbSurvey dbSurvey);

}
