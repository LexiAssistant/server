package dev.changuii.project.controller;

import dev.changuii.project.entity.AnalysisDTO;
import dev.changuii.project.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(
            @Autowired AnalysisService analysisService
    ){
        this.analysisService = analysisService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<AnalysisDTO> analysis(
            @PathVariable("id") Long imageId
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(this.analysisService.analysisImage(imageId));
    }
}
