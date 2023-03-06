package com.intellisoft.nationalinstance.db.repso;

import com.intellisoft.nationalinstance.db.SurveyRespondents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyRespondentsRepo extends JpaRepository<SurveyRespondents, Long> {
    List<SurveyRespondents> findAllBySurveyId(String surveyId);
    void deleteAllBySurveyId(String surveyId);
}
