package com.intellisoft.internationalinstance.service_impl;

import com.intellisoft.internationalinstance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class ProgramsServiceImpl implements ProgramsService{

    @Autowired
    private RestTemplate restTemplate;

    @Value("${dhis.international}")
    private String internationalUrl;

    @Value("${dhis.username}")
    private String username;
    @Value("${dhis.password}")
    private String password;

    @Value("${dhis.programs}")
    private String programsUrl;
    @Value("${dhis.datastore}")
    private String dataStore;

    private HttpEntity<String> getHeaders(){


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " +
                Base64Utils.encodeToString("admin:district".getBytes()));

        return new HttpEntity<>(headers);

    }

    @Override
    public Results programList() {

        Results results;


        ResponseEntity<DbProgramsList> response = restTemplate.exchange(internationalUrl+programsUrl,
                HttpMethod.GET, getHeaders(), DbProgramsList.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // process the response
            DbProgramsList dbProgramsList = response.getBody();
            if (dbProgramsList != null){
                List<DbPrograms> programsList = dbProgramsList.getPrograms();
                DbResults dbResults = new DbResults(
                        programsList.size(),
                        programsList);
                results = new Results(200, dbResults);
            }else {
                results = new Results(400, new DbError("No programs could be found"));
            }
        } else {
            // handle error
            results = new Results(400, new DbError("There was an issue getting the programs."));
        }
        return results;
    }

    @Override
    public Results getNamespaces() {


        Results results;

        ResponseEntity<String[]> response = getNamespaceData();
        if (response.getStatusCode() == HttpStatus.OK) {
            // process the response
            String[] list = response.getBody();
            if (list != null){
                DbResults dbResults = new DbResults(
                        list.length,
                        list);
                results = new Results(200, dbResults);
            }else {
                results = new Results(400, new DbError("No namespaces could be found"));
            }

        } else {
            // handle error
            results = new Results(400, new DbError("There was an issue getting the namespaces."));
        }
        return results;
    }

    private ResponseEntity<String[]> getNamespaceData(){


        String nameSpaceUrl = internationalUrl + dataStore;

        return restTemplate.exchange(nameSpaceUrl,
                HttpMethod.GET, getHeaders(), String[].class);

    }

    @Override
    public Results getVersions(String nameSpace) {

        Results results;

        ResponseEntity<String[]> response = getVersionsData(nameSpace);
        if (response.getStatusCode() == HttpStatus.OK) {
            // process the response
            String[] list = response.getBody();
            if (list != null){
                DbResults dbResults = new DbResults(
                        list.length,
                        list);
                results = new Results(200, dbResults);
            }else {
                results = new Results(400, new DbError("No versions could be found under this namespace"));
            }

        } else {
            // handle error
            results = new Results(400, new DbError("There was an issue getting the versions."));
        }
        return results;
    }

    private ResponseEntity<String[]> getVersionsData(String namespace){

        String versionUrl = internationalUrl + dataStore + namespace;
        ResponseEntity<String[]> response = restTemplate.exchange(versionUrl,
                HttpMethod.GET, getHeaders(), String[].class);
        return response;
    }

    @Override
    public Results getTemplates(int limitNo) {

        String dataStoreUrl = internationalUrl + dataStore;
        List<DbTemplateData> dbTemplateDataList = new ArrayList<>();

        /**
         * Get namespaces, from individual namespace, get versions under the namespace
         * Using THE namespace and THE version, get the datastore and format
         */
        ResponseEntity<String[]> namespaceDataList = getNamespaceData();
        if (namespaceDataList.getStatusCode() == HttpStatus.OK) {
            // process the response
            String[] nameSpaceList = namespaceDataList.getBody();
            if (nameSpaceList != null){
                ArrayList<String> namespaceList = new ArrayList<>(List.of(nameSpaceList));
                for (int i = 0; i < namespaceList.size(); i++){
                    // Get individual namespace
                    String namespace = namespaceList.get(i);
                    ResponseEntity<String[]> versionList = getVersionsData(namespace);
                    if (versionList.getStatusCode() == HttpStatus.OK ){

                        String[] versionDataList = versionList.getBody();
                        if (versionDataList != null){

                            ArrayList<String> versionValuesList = new ArrayList<>(List.of(versionDataList));
                            for (int j = 0; j < versionValuesList.size(); j++){
                                //Get individual version
                                String versionValue = versionValuesList.get(j);
                                String templateUrl = dataStoreUrl + namespace + "/" + versionValue;
                                System.out.println(templateUrl);

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

                }

            }

        }

        //Truncate the list to the provided limit


        DbResults dbResults = new DbResults(
                dbTemplateDataList.size(),
                dbTemplateDataList);

        return new Results(200, dbResults);

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

}
