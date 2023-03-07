package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.*;
import com.intellisoft.nationalinstance.db.SurveyRespondents;
import com.intellisoft.nationalinstance.db.Surveys;
import com.intellisoft.nationalinstance.db.repso.SurveyRespondentsRepo;
import com.intellisoft.nationalinstance.db.repso.SurveysRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveysServiceImpl implements SurveysService{

    private final SurveysRepo surveysRepo;
    private final SurveyRespondentsRepo respondentsRepo;

    @Override
    public Results addSurvey(DbSurvey dbSurvey) {

        String surveyName = dbSurvey.getSurveyName();
        String surveyDescription = dbSurvey.getSurveyDescription();
        String status = dbSurvey.getStatus();
        String creatorId = dbSurvey.getCreatorId();
        List<String> indicatorList = dbSurvey.getIndicators();

        Surveys surveys = new Surveys();
        surveys.setName(surveyName);
        surveys.setDescription(surveyDescription);
        surveys.setStatus(status);
        surveys.setCreatorId(creatorId);
        surveys.setIndicators(indicatorList);
        surveysRepo.save(surveys);

        return new Results(201, surveys);
    }

    @Override
    public Results listAdminSurveys(String creatorId) {

        List<Surveys> surveysList = surveysRepo.findAllByCreatorId(creatorId);
        DbResults dbResults = new DbResults(
                surveysList.size(),
                surveysList);

        return new Results(200, dbResults);
    }

    @Override
    public Results listRespondentsSurveys(String creatorId) {
        List<DbSurveyRespondentDetails> dbSurveyRespondentDetailsList = new ArrayList<>();
        List<Surveys> surveysList = surveysRepo.findAllByCreatorId(creatorId);
        for (int i = 0; i < surveysList.size(); i++){

            String surveyId = String.valueOf(surveysList.get(i).getId());
            String surveyName = surveysList.get(i).getName();
            List<SurveyRespondents> respondentsList =
                    respondentsRepo.findAllBySurveyId(surveyId);

            List<DbRespondentDetails> dbRespondentDetailsList = new ArrayList<>();
            for (int j = 0; j< respondentsList.size(); j++){

                String respondentId = String.valueOf(respondentsList.get(j).getId());
                String emailAddress = respondentsList.get(j).getEmailAddress();
                String createdAt = String.valueOf(respondentsList.get(j).getCreatedAt());

                DbRespondentDetails dbRespondentDetails = new DbRespondentDetails(
                        respondentId,
                        emailAddress,
                        createdAt);
                dbRespondentDetailsList.add(dbRespondentDetails);

            }
            DbSurveyRespondentDetails dbSurveyRespondentDetails = new DbSurveyRespondentDetails(
                    surveyId,
                    surveyName,
                    dbRespondentDetailsList);
            dbSurveyRespondentDetailsList.add(dbSurveyRespondentDetails);

        }
        DbResults dbResults = new DbResults(
                dbSurveyRespondentDetailsList.size(),
                dbSurveyRespondentDetailsList);

        return new Results(200, dbResults);
    }

    @Override
    public Results surveyDetails(String id) {

        Optional<Surveys> optionalSurveys =
                surveysRepo.findById(Long.valueOf(id));
        return optionalSurveys.map(surveys ->
                new Results(200, surveys))
                .orElse(
                        new Results(400, "Resource not found.")
                );

    }

    @Override
    public Results deleteSurvey(Long id) {
        Optional<Surveys> optionalSurveys =
                surveysRepo.findById(id);
        if (optionalSurveys.isPresent()){
            /**
             * TODO: DELETE survey respondents
             */
            String name = optionalSurveys.get().getName();
            surveysRepo.deleteById(id);
            return new Results(200, new DbDetails(
                    name + " has been deleted successfully."
            ));
        }
        return new Results(400, "Survey not found");
    }

    @Override
    public Results updateSurvey(Long id, DbSurvey dbSurvey) {

        Surveys surveys = surveysRepo.findById(id)
                .map(surveysOld ->{
                    surveysOld.setName(dbSurvey.getSurveyName());
                    surveysOld.setDescription(dbSurvey.getSurveyDescription());
                    surveysOld.setStatus(dbSurvey.getStatus());
                    surveysOld.setIndicators(dbSurvey.getIndicators());
                    return surveysRepo.save(surveysOld);
                } ).orElse(null);

        if (surveys != null){
            return new Results(200, new DbDetails("Survey has been updated successfully."));
        }

        return new Results(400, "Survey could not be updated");
    }
}
