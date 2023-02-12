package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class NationalServiceImpl implements NationalService{

    @Value("${dhis.international}")
    private String internationalUrl;

    @Value("${dhis.datastore}")
    private String dataStore;

    @Value("${dhis.template}")
    private String master_template;

    @Autowired
    private RestTemplate restTemplate;

    private HttpEntity<String> getHeaders(){


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " +
                Base64Utils.encodeToString("admin:district".getBytes()));

        return new HttpEntity<>(headers);

    }

    @Override
    public Results getOrganisationUnits() {
        return null;
    }

    @Override
    public Results getVersions(int limitNo) {

        List<DbTemplateData> versionList = getTemplates();
        //Truncate as per the limit no

        DbResults dbResults = new DbResults(
                versionList.size(),
                versionList);

        return new Results(200, dbResults);

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

                    DbTemplate dbTemplate = getTemplateData(templateUrl);
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

    private DbTemplate getTemplateData(String templateUrl){

        ResponseEntity<DbTemplate> response = restTemplate.exchange(templateUrl,
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


    private ResponseEntity<String[]> getVersionsData(String namespace){

        String versionUrl = internationalUrl + dataStore + namespace;
        return restTemplate.exchange(versionUrl,
                HttpMethod.GET, getHeaders(), String[].class);
    }

    @Override
    public Results saveVersions(DbDataEntry dataEntry) {
        return null;
    }
}
