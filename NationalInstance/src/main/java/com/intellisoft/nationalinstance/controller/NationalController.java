package com.intellisoft.nationalinstance.controller;

import com.intellisoft.nationalinstance.DbDataEntry;
import com.intellisoft.nationalinstance.FormatterClass;
import com.intellisoft.nationalinstance.Results;
import com.intellisoft.nationalinstance.service_impl.NationalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/v1")
@RestController
@RequiredArgsConstructor
public class NationalController {

    FormatterClass formatterClass = new FormatterClass();

    private final NationalService nationalService;

    @GetMapping(value = "/organisation-unit")
    public ResponseEntity<?> getOrganisationUnit(){

        Results results = nationalService.getOrganisationUnits();
        return formatterClass.getResponse(results);

    }
    @GetMapping(value = "/versions")
    public ResponseEntity<?> getVersions(
            @RequestParam(value = "limit", required = false) String limit

    ){
        int limitNo = 10;
        if (limit != null && !limit.equals("")){
            limitNo = Integer.parseInt(limit);
        }

        Results results = nationalService.getVersions(limitNo);
        return formatterClass.getResponse(results);

    }

    @PostMapping("/save-data-entry")
    public ResponseEntity<?> saveDataEntry(
            @RequestBody DbDataEntry dataEntry) {

        Results results = nationalService.saveVersions(dataEntry);
        return formatterClass.getResponse(results);

    }


}
