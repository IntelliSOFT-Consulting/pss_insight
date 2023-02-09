package com.intellisoft.internationalinstance.controller;

import com.intellisoft.internationalinstance.FormatterClass;
import com.intellisoft.internationalinstance.Results;
import com.intellisoft.internationalinstance.service_impl.ProgramsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/v1")
@RestController
@RequiredArgsConstructor
public class DhisController {

    FormatterClass formatterClass = new FormatterClass();

    private final ProgramsService programsService;

    @GetMapping(value = "/programs")
    public ResponseEntity<?> getPrograms(){

        Results results = programsService.programList();
        return formatterClass.getResponse(results);

    }

    // Get available namespaces
    @GetMapping(value = "/namespaces")
    public ResponseEntity<?> getNamespaces(){

        Results results = programsService.getNamespaces();
        return formatterClass.getResponse(results);

    }

    // Get available versions under a namespace
    @GetMapping(value = "/versions/{namespace}")
    public ResponseEntity<?> getVersions(@PathVariable("namespace") String namespace){

        Results results = programsService.getVersions(namespace);
        return formatterClass.getResponse(results);

    }
    // Get Templates
    @GetMapping(value = "/master-templates")
    public ResponseEntity<?> getTemplates(
            @RequestParam(value = "limit", required = false) String limit
    ){

        int limitNo = 10;
        if (limit != null && !limit.equals("")){
            limitNo = Integer.parseInt(limit);
        }

        Results results = programsService.getTemplates(limitNo);
        return formatterClass.getResponse(results);

    }

}

