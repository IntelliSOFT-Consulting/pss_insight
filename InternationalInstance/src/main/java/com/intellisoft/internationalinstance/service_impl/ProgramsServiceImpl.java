package com.intellisoft.internationalinstance.service_impl;

import com.intellisoft.internationalinstance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

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

    private HttpEntity<String> getHeaders(){


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " +
                Base64Utils.encodeToString("admin:district".getBytes()));

        return new HttpEntity<>(headers);

    }

    @Override
    public Results programList() {

        Results results;

        String url = "programs/T4EBleGG9mU/metadata.json";

        ResponseEntity<DbProgramsList> response = restTemplate.exchange(internationalUrl+url,
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

    public static String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
}
