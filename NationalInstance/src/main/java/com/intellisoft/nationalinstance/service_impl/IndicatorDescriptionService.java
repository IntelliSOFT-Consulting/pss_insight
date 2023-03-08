package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.db.IndicatorDescription;

import java.net.URISyntaxException;

public interface IndicatorDescriptionService {
    IndicatorDescription addIndicatorDescription(IndicatorDescription indicatorDescription);
    IndicatorDescription getIndicatorDescriptionByCode(String code);

    void getIndicatorDescription() throws URISyntaxException;
}
