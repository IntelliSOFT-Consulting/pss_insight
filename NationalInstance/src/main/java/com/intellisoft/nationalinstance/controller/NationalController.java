package com.intellisoft.nationalinstance.controller;

import com.intellisoft.nationalinstance.DbDataElementsValue;
import com.intellisoft.nationalinstance.DbDataEntry;
import com.intellisoft.nationalinstance.FormatterClass;
import com.intellisoft.nationalinstance.Results;
import com.intellisoft.nationalinstance.db.IndicatorDescription;
import com.intellisoft.nationalinstance.service_impl.IndicatorDescriptionService;
import com.intellisoft.nationalinstance.service_impl.NationalService;
import com.intellisoft.nationalinstance.service_impl.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/v1/national-instance")
@RestController
@RequiredArgsConstructor
public class NationalController {

    FormatterClass formatterClass = new FormatterClass();

    private final NationalService nationalService;
    private final VersionService versionService;
    private final IndicatorDescriptionService indicatorDescriptionService;

    @GetMapping(value = "/organisation-unit")
    public ResponseEntity<?> getOrganisationUnit(){

        Results results = nationalService.getOrganisationUnits();
        return formatterClass.getResponse(results);

    }

    /**
     * Pull all the indicators from the international data store and display to frontend
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/indicators")
    public ResponseEntity<?> getIndicatorForFrontEnd() throws URISyntaxException {
        Results results = versionService.getIndicators();
        return formatterClass.getResponse(results);
    }

    /**
     * Get Indicator Details
     */
    @GetMapping(value = "/indicator-description/{indicatorCode}")
    public ResponseEntity<?> getIndicatorDescriptionByCode(@PathVariable("indicatorCode") String indicatorCode){
        Results results;
        IndicatorDescription indicatorDescription = indicatorDescriptionService.getIndicatorDescriptionByCode(indicatorCode);
        if (indicatorDescription != null){
            results = new Results(200, indicatorDescription);
        }else {
            results = new Results(400, "Indicator description not found.");
        }
        return formatterClass.getResponse(results);

    }
//    @GetMapping(value = "/versions")
//    public ResponseEntity<?> getVersions(
//            @RequestParam(value = "limit", required = false) String limit
//
//    ){
//        int limitNo = 10;
//        if (limit != null && !limit.equals("")){
//            limitNo = Integer.parseInt(limit);
//        }
//
//        Results results = nationalService.getVersions(limitNo);
//        return formatterClass.getResponse(results);
//
//    }
//
//    @GetMapping(value = "/data-elements/{version}")
//    public ResponseEntity<?> getVersionsQuestions(
//            @PathVariable("version") String version){
//
//        Results results = nationalService.getVersionDataElements(version);
//        return formatterClass.getResponse(results);
//
//    }
//
//
//
//    @PostMapping("/save-data-entry")
//    public ResponseEntity<?> saveDataEntry(
//            @RequestBody DbDataEntry dataEntry) {
//
//        Results results = nationalService.saveVersions(dataEntry);
//        return formatterClass.getResponse(results);
//
//    }


}
