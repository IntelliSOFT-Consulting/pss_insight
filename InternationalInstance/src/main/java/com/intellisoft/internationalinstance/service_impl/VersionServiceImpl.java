package com.intellisoft.internationalinstance.service_impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.intellisoft.internationalinstance.db.Indicators;
import com.intellisoft.internationalinstance.db.VersionEntity;
import com.intellisoft.internationalinstance.db.repso.IndicatorsRepo;
import com.intellisoft.internationalinstance.db.repso.VersionRepos;
import com.intellisoft.internationalinstance.exception.CustomException;
import com.intellisoft.internationalinstance.model.IndicatorForFrontEnd;
import com.intellisoft.internationalinstance.model.Response;
import com.intellisoft.internationalinstance.util.AppConstants;
import com.intellisoft.internationalinstance.util.GenericWebclient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class VersionServiceImpl implements VersionService {
    private final IndicatorsRepo indicatorsRepo;
    private final VersionRepos versionRepos;
    @Override
    public List<IndicatorForFrontEnd> getIndicators() throws URISyntaxException {
        List<IndicatorForFrontEnd> indicatorForFrontEnds = new LinkedList<>();
        List<Indicators> indicators = getDataFromRemote();
        indicators.forEach(indicator -> {
            JSONObject jsonObject = new JSONObject(indicator.getMetadata());
            try {
                String id = jsonObject.getString("id");
                String code = jsonObject.getString("code");
                String formName = jsonObject.getString("formName");
                indicatorForFrontEnds.add(new IndicatorForFrontEnd(id, code, formName));
            } catch (JSONException e) {
                log.info(e.getMessage());
            }

        });

        return indicatorForFrontEnds;
    }

    @Override
    public VersionEntity saveDraftOrPublish(VersionEntity version) throws URISyntaxException {
        var vs = versionRepos.findByVersionName(version.getVersionName());
        if (vs.isPresent()) {
            VersionEntity versionEntity = vs.get();
            versionEntity.setIsPublished(version.getIsPublished());
            versionEntity.setVersionDescription(version.getVersionDescription());
            versionEntity.setIndicators(version.getIndicators());
            version = versionEntity;
        }
        if (Boolean.TRUE.equals(version.getIsPublished()))
        {
            List<String> metaData = indicatorsRepo.findByIndicatorIds(version.getIndicators());
            if (!metaData.isEmpty()) {
                JSONObject jsonObject = getRawRemoteData();
                jsonObject.put("dataElements", new JSONArray(metaData));
                jsonObject.put("version", version.getVersionName());
                var response = GenericWebclient.postForSingleObjResponse(AppConstants.DATA_STORE_ENDPOINT+version.getVersionName(), jsonObject, JSONObject.class, Response.class);
                log.info("RESPONSE FROM REMOTE: {}",response.toString());
                if (response.getHttpStatusCode() < 200) {
                    throw new CustomException("Unable to create/update record on data store"+response);
                }
            }
            else {
                throw new CustomException("No indicators found for the ids given"+version.getIndicators());
            }

        }

        return versionRepos.save(version);
    }

    private List<Indicators> getDataFromRemote() throws URISyntaxException {
        List<Indicators> indicators = new LinkedList<>();
        var  res =GenericWebclient.getForSingleObjResponse(AppConstants.METADATA_ENDPOINT, String.class);

        JSONObject jsObject = new JSONObject(res);
        JSONArray dataElements = jsObject.getJSONArray("dataElements");
        dataElements.forEach(element->{
            String  id = ((JSONObject)element).getString("id");
            Indicators indicator = new Indicators();
            indicator.setIndicatorId(id);
            indicator.setMetadata(element.toString());
            indicators.add(indicator);

        });

        return Lists.newArrayList(indicatorsRepo.saveAll(indicators));

    }
    private JSONObject getRawRemoteData() throws URISyntaxException {
        var  res =GenericWebclient.getForSingleObjResponse(AppConstants.METADATA_ENDPOINT, String.class);
        return new  JSONObject(res);
    }
}
