package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class NationalServiceImpl implements NationalService{

    @Value("${dhis.international}")
    private String internationalUrl;
    @Value("${dhis.national}")
    private String nationalUrl;

    @Value("${dhis.orgUnits}")
    private String orgUnitsUrl;

    @Value("${dhis.events}")
    private String events;

    @Value("${dhis.datastore}")
    private String dataStore;

    @Value("${dhis.template}")
    private String master_template;

    @Autowired
    private RestTemplate restTemplate;

    FormatterClass formatterClass = new FormatterClass();

    private HttpEntity<String> getHeaders(){


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " +
                Base64Utils.encodeToString("admin:district".getBytes()));

        return new HttpEntity<>(headers);

    }

    @Override
    public Results getOrganisationUnits() {

        DbOrganisationUnit dbOrganisationUnit = getOrganisationUnitData();
        if(dbOrganisationUnit != null){

            List<DbOrgUnits> dbOrgUnitsList = dbOrganisationUnit.getOrganisationUnits();

            DbResults dbResults = new DbResults(
                    dbOrgUnitsList.size(),
                    dbOrgUnitsList);

            return new Results(200, dbResults);

        }else {
            return new Results(400, new DbError("There was an issue getting the org units"));
        }
    }

    private DbOrganisationUnit getOrganisationUnitData(){

        String orgUrl = nationalUrl + orgUnitsUrl;
        ResponseEntity<DbOrganisationUnit> response = restTemplate.exchange(orgUrl,
                HttpMethod.GET, getHeaders(), DbOrganisationUnit.class);

        try{

            if (response.getStatusCode() == HttpStatus.OK) {
                // process the response
                DbOrganisationUnit dbTemplate = response.getBody();
                if (dbTemplate != null) {
                    return dbTemplate;
                }else {
                    return null;
                }
            }else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }



    }


    @Override
    public Results getVersions(int limitNo) {

        List<DbTemplateData> versionList = getTemplates();
        //Truncate as per the limit no

        DbResults dbResults = new DbResults(
                versionList.size(),
                versionList);

        //Check on this

        return new Results(200, dbResults);

    }

    @Override
    public Results getVersionDataElements(String version) {

        String metadataUrl = internationalUrl + dataStore + master_template + "/" + version;
        DbTemplate dbTemplate = getDbTemplate(metadataUrl);
        if (dbTemplate != null){
            DbMetaData dbMetaData = dbTemplate.getMetadata();
            if (dbMetaData != null){
                ArrayList<DbDataElementData> dataElementList = dbMetaData.getBody().getDataElements();
                DbResults dbResults = new DbResults(
                        dataElementList.size(),
                        dataElementList);
                return new Results(200, dbResults);
            }else {
                DbResults dbResults = new DbResults(
                        0,
                        new ArrayList<DbDataElementData>());
                return new Results(200, dbResults);
            }
        }else{
            return new Results(400, new DbError("We could not find the resource"));
        }
    }



    @Nullable
    private DbTemplate getDbTemplate(String metadataUrl) {

        System.out.println(metadataUrl);

        ResponseEntity<DbTemplate> response = restTemplate.exchange(metadataUrl,
                HttpMethod.GET, getHeaders(), DbTemplate.class);

        try{

            if (response.getStatusCode() == HttpStatus.OK) {
                // process the response
                DbTemplate dbTemplate = response.getBody();
                if (dbTemplate != null) {
                    return dbTemplate;
                }else {
                    return null;
                }
            }else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<DbTemplateData> getTemplates() {

        String dataStoreUrl = internationalUrl + dataStore;
        List<DbTemplateData> dbTemplateDataList = new ArrayList<>();

        /**
         * Use the one namespace, get versions under the namespace
         * Using THE namespace and THE version, get the datastore and format
         */
        ResponseEntity<String[]> versionList = getVersionsData(master_template);
        if (versionList.getStatusCode() == HttpStatus.OK ){

            String[] versionDataList = versionList.getBody();
            if (versionDataList != null){

                ArrayList<String> versionValuesList = new ArrayList<>(List.of(versionDataList));
                for (int j = 0; j < versionValuesList.size(); j++){
                    //Get individual version
                    String versionValue = versionValuesList.get(j);
                    String templateUrl = dataStoreUrl + master_template + "/" + versionValue;

                    DbTemplate dbTemplate = getDbTemplate(templateUrl);
                    if (dbTemplate != null){

                        String description = "";
                        String program = "";

                        if (dbTemplate.getDescription() != null){
                            description = dbTemplate.getDescription();
                        }
                        if (dbTemplate.getProgram() != null){
                            program = dbTemplate.getProgram();
                        }

                        DbTemplateData dbTemplateData = new DbTemplateData(
                                versionValue,
                                description,
                                program);
                        dbTemplateDataList.add(dbTemplateData);
                    }

                }

            }

        }

        //Truncate the list to the provided limit
        return dbTemplateDataList;

    }



    private ResponseEntity<String[]> getVersionsData(String namespace){

        String versionUrl = internationalUrl + dataStore + namespace;
        return restTemplate.exchange(versionUrl,
                HttpMethod.GET, getHeaders(), String[].class);
    }

    @Override
    public Results saveVersions(DbDataEntry dataEntry) {

        Results results;

        // check if the date is correct
        String date = dataEntry.getEventDate();
        boolean isDateValid = formatterClass.isValidDate(date);

        if (isDateValid){

            String saveUrl = nationalUrl + events;

            // Create the request headers
            HttpHeaders headers = new HttpHeaders();
            String auth = "admin:district";
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            String authHeader = "Basic " + new String(encodedAuth);
            headers.add("Authorization", authHeader);

            // Create the request body as a Map
            HttpEntity<DbDataEntry> request = new HttpEntity<>(dataEntry, headers);

            ResponseEntity<DbDataEntrySave> response = restTemplate.postForEntity(
                    saveUrl, request, DbDataEntrySave.class);

            if (response.getStatusCodeValue() == 200){
                results = new Results(201, new DbError("The data has been saved successfully."));
            }else {
                results = new Results(400, new DbError("The resource cannot be saved"));
            }


        }else {
            results = new Results(400, new DbError("Make sure the event date is in the format yyyy-mm-dd"));

        }


        return results;

    }
}
