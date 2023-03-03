package com.intellisoft.nationalinstance.service_impl;

import com.google.common.collect.Lists;
import com.intellisoft.nationalinstance.*;
import com.intellisoft.nationalinstance.DbVersionData;
import com.intellisoft.nationalinstance.PublishStatus;
import com.intellisoft.nationalinstance.Results;
import com.intellisoft.nationalinstance.db.Indicators;
import com.intellisoft.nationalinstance.db.VersionEntity;
import com.intellisoft.nationalinstance.db.repso.IndicatorsRepo;
import com.intellisoft.nationalinstance.db.repso.VersionRepos;
import com.intellisoft.nationalinstance.exception.CustomException;
import com.intellisoft.nationalinstance.model.IndicatorForFrontEnd;
import com.intellisoft.nationalinstance.model.Response;
import com.intellisoft.nationalinstance.util.AppConstants;
import com.intellisoft.nationalinstance.util.GenericWebclient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class VersionServiceImpl implements VersionService {
    private final IndicatorsRepo indicatorsRepo;
    private final VersionRepos versionRepos;
//    @Override
//    public List<IndicatorForFrontEnd> getIndicators() throws URISyntaxException {
//        List<Indicators> indicators = getDataFromRemote();
//        return extractIndicators(indicators);
//    }

    @Override
    public List<IndicatorForFrontEnd> extractIndicators(List<Indicators> indicators) {
        List<IndicatorForFrontEnd> indicatorForFrontEnds = new LinkedList<>();
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
    public VersionEntity saveDraftOrPublish(DbVersionData dbVersionData) throws URISyntaxException {

        VersionEntity version = new VersionEntity();

        String versionDescription = dbVersionData.getVersionDescription();
        boolean isPublished = dbVersionData.isPublished();
        List<String> indicatorList = dbVersionData.getIndicators();

        String createdBy = dbVersionData.getCreatedBy();
        String publishedBy = dbVersionData.getPublishedBy();

        String status = PublishStatus.DRAFT.name();
        if (isPublished){
            status = PublishStatus.PUBLISHED.name();
        }

        String versionNo = "1";

        if (dbVersionData.getVersionId() != null){
            long versionId = dbVersionData.getVersionId();
            var vs = versionRepos.findById(versionId);
            if (vs.isPresent()) {
                VersionEntity versionEntity = vs.get();
                versionEntity.setStatus(status);
                versionEntity.setVersionDescription(versionDescription);
                versionEntity.setIndicators(indicatorList);
                versionNo = versionEntity.getVersionName();

                version = versionEntity;
            }


        }else {
            //Generate new version name
            Long currentId = versionRepos.findLatestId();
            if (currentId != null){
                long nextId = versionRepos.findLatestId() + 1;
                versionNo = String.valueOf(nextId);
            }

        }

        //Set the version number
        version.setVersionName(versionNo);
        version.setIndicators(indicatorList);
        version.setVersionDescription(versionDescription);
        version.setStatus(status);
        if (createdBy != null){
            version.setCreatedBy(createdBy);
        }

        //Check if we're required to publish
        if(isPublished){

            List<String> metaData = indicatorsRepo.findMetadataByIndicatorIds(indicatorList);
            if (!metaData.isEmpty()) {
                JSONObject jsonObject = getRawRemoteData();
                jsonObject.put("dataElements", new JSONArray(metaData));
                jsonObject.put("version", versionNo);

                var response = GenericWebclient.postForSingleObjResponse(
                        AppConstants.DATA_STORE_ENDPOINT+versionNo,
                        jsonObject,
                        JSONObject.class,
                        Response.class);
                log.info("RESPONSE FROM REMOTE: {}",response.toString());
                if (response.getHttpStatusCode() < 200) {
                    throw new CustomException("Unable to create/update record on data store"+response);
                }else {
                    versionRepos.updateAllIsPublishedToFalse(PublishStatus.DRAFT.name());

                    version.setStatus(PublishStatus.PUBLISHED.name());
                    if (publishedBy != null){
                        version.setPublishedBy(publishedBy);
                    }
                }
            }
            else {
                throw new CustomException("No indicators found for the ids given"+indicatorList);
            }

        }

        return versionRepos.save(version);

    }

    @Override
    public Results getTemplates(int page, int size, String status) {

//        List<VersionEntity> versionEntityList =
//                getPagedTemplates(
//                        page,
//                        size,
//                        "",
//                        "",
//                        status);

        List<VersionEntity> versionEntityList = (List<VersionEntity>) versionRepos.findAll();

        DbResults dbResults = new DbResults(
                versionEntityList.size(),
                versionEntityList);

        return new Results(200, dbResults);
    }

    @Override
    public Results deleteTemplate(long deleteId) {
        Results results;

        Optional<VersionEntity> optionalVersionEntity =
                versionRepos.findById(deleteId);
        if (optionalVersionEntity.isPresent()){
            versionRepos.deleteById(deleteId);
            results = new Results(200, new DbDetails(
                    optionalVersionEntity.get().getVersionName() + " has been deleted successfully."
            ));
        }else {
            results = new Results(400, "The id cannot be found.");
        }


        return results;
    }

    @Override
    public Results getVersion(long versionId) {
        /**
         * TODO: Check on the 2 tier groupings
         * Categories and the Indicator names
         *
         */

        Results results;

        Optional<VersionEntity> optionalVersionEntity =
                versionRepos.findById(versionId);

        if (optionalVersionEntity.isPresent()){

            VersionEntity versionEntity = optionalVersionEntity.get();
            List<String> entityIndicators = versionEntity.getIndicators();

            List<String> metaDataList = indicatorsRepo.findMetadataByIndicatorIds(entityIndicators);

            DbIndicatorValues dbIndicatorValues = new DbIndicatorValues(
                    versionEntity.getVersionName(),
                    versionEntity.getVersionDescription(),
                    versionId,
                    versionEntity.getStatus(),
                    metaDataList);

            results = new Results(200, dbIndicatorValues);

        }else {
            results = new Results(400, "Version could not be found.");
        }
        return results;
    }

    @Override
    public Results getIndicators() throws URISyntaxException {

        /**
         * TODO: Create the groups that will be used for the indicators
         */

        List<IndicatorForFrontEnd> indicatorForFrontEnds = new LinkedList<>();

        try{

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

        }catch (Exception e){
            e.printStackTrace();
        }

        DbResults dbResults = new DbResults(
                indicatorForFrontEnds.size(),
                indicatorForFrontEnds);

        return new Results(200, dbResults);

    }


    @Override
    public Response syncVersion() throws URISyntaxException {

        /**
         * Get the international data from the metadata json
         */
        var jsonObject = GenericWebclient.getForSingleObjResponse(
                AppConstants.INTERNATIONAL_METADATA_ENDPOINT,
                String.class);

        /**
         * TODO: 27/02/2023 post to national instance
         * Post the date to the National instance
         *
         */

        Response response = GenericWebclient.postForSingleObjResponse(
                AppConstants.DATA_STORE_ENDPOINT+ UUID.randomUUID().toString().split("-")[0],
                new JSONObject(jsonObject),
                JSONObject.class,
                Response.class);


        log.info("RESPONSE FROM REMOTE: {}",response.toString());
        if (response.getHttpStatusCode() < 200) {
            throw new CustomException("Unable to create/update record on data store"+response);
        }
        return response;

    }

    private List<Indicators> getDataFromRemote() throws URISyntaxException {
        List<Indicators> indicators = new LinkedList<>();
        var  res =GenericWebclient.getForSingleObjResponse(AppConstants.INTERNATIONAL_METADATA_ENDPOINT, String.class);

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
        //change to national url
        var  res =GenericWebclient.getForSingleObjResponse(AppConstants.INTERNATIONAL_METADATA_ENDPOINT, String.class);
        return new  JSONObject(res);
    }
}
