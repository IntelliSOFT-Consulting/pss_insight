package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.DbDetails;
import com.intellisoft.nationalinstance.DbResults;
import com.intellisoft.nationalinstance.DbSurvey;
import com.intellisoft.nationalinstance.Results;
import com.intellisoft.nationalinstance.db.Surveys;
import com.intellisoft.nationalinstance.db.repso.SurveysRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveysServiceImpl implements SurveysService{

    private final SurveysRepo surveysRepo;

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


        return new Results(201, surveys);
    }

    @Override
    public Results listSurveys(String creatorId) {

        List<Surveys> surveysList = surveysRepo.findAllByCreatorId(creatorId);
        DbResults dbResults = new DbResults(
                surveysList.size(),
                surveysList);

        return new Results(200, dbResults);
    }

    @Override
    public Results surveyDetails(Long id) {

        Optional<Surveys> optionalSurveys =
                surveysRepo.findById(id);
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
