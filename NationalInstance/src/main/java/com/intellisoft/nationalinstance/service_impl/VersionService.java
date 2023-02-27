package com.intellisoft.nationalinstance.service_impl;


import com.intellisoft.nationalinstance.db.Indicators;
import com.intellisoft.nationalinstance.db.VersionEntity;
import com.intellisoft.nationalinstance.model.IndicatorForFrontEnd;
import com.intellisoft.nationalinstance.model.Response;

import java.net.URISyntaxException;
import java.util.List;

public interface VersionService {
 List<IndicatorForFrontEnd> getIndicators() throws URISyntaxException;
 VersionEntity saveDraftOrPublish(VersionEntity version) throws URISyntaxException;
 Response syncVersion() throws URISyntaxException;
 List<IndicatorForFrontEnd> extractIndicators(List<Indicators> indicators);
}
