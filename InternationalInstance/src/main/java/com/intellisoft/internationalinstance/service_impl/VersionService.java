package com.intellisoft.internationalinstance.service_impl;

import com.intellisoft.internationalinstance.db.VersionEntity;
import com.intellisoft.internationalinstance.model.IndicatorForFrontEnd;

import java.net.URISyntaxException;
import java.util.List;

public interface VersionService {
 List<IndicatorForFrontEnd> getIndicators() throws URISyntaxException;
 VersionEntity saveDraftOrPublish(VersionEntity version) throws URISyntaxException;
}
