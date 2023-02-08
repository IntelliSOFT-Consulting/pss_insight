package com.intellisoft.internationalinstance.controller;

import com.intellisoft.internationalinstance.FormatterClass;
import com.intellisoft.internationalinstance.Results;
import com.intellisoft.internationalinstance.service_impl.ProgramsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/v1")
@RestController
@RequiredArgsConstructor
public class DhisController {

    FormatterClass formatterClass = new FormatterClass();

    private final ProgramsService programsService;

    @GetMapping(value = "/programs")
    public ResponseEntity<?> getAppointmentTypes(){

        Results results = programsService.programList();
        return formatterClass.getResponse(results);
//https://github.com/IntelliSOFT-Consulting/pss_insight

    }
}

//    git remote add origin https://github.com/IntelliSOFT-Consulting/pss_insight

