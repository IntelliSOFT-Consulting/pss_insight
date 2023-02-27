package com.intellisoft.nationalinstance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyQuestions {
    private List<IndicatorForFrontEnd> indicatorForFrontEnds;
    private Long surveyId;
}
