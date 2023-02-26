package com.intellisoft.internationalinstance.controller;

import com.intellisoft.internationalinstance.db.VersionEntity;
import com.intellisoft.internationalinstance.model.IndicatorForFrontEnd;
import com.intellisoft.internationalinstance.service_impl.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/v1")
@RestController
@RequiredArgsConstructor
public class MyController {
    private final VersionService versionService;
    @GetMapping("/programs1")
    public List<IndicatorForFrontEnd> getIndicatorForFrontEnd() throws URISyntaxException {
        return versionService.getIndicators();
    }
    @PostMapping("/create-version1")
    public VersionEntity createVersion(@RequestBody VersionEntity versionEntity) throws URISyntaxException {
        return versionService.saveDraftOrPublish(versionEntity);
    }
}
