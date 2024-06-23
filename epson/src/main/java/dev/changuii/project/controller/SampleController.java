package dev.changuii.project.controller;


import dev.changuii.project.entity.AnalysisDTO;
import dev.changuii.project.entity.ImageEntity;
import dev.changuii.project.exception.CustomException;
import dev.changuii.project.repository.ImageRepository;
import dev.changuii.project.service.AnalysisService;
import dev.changuii.project.service.TesseractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/sample")
public class SampleController {


    private TesseractService tesseractService;
    private final ImageRepository imageRepository;
    private final AnalysisService analysisService;

    public SampleController(
            @Autowired ImageRepository imageRepository,
            @Autowired TesseractService tesseractService,
            @Autowired AnalysisService analysisService
    ){
        this.tesseractService = tesseractService;
        this.imageRepository=imageRepository;
        this.analysisService = analysisService;
    }


    @PostMapping("")
    public String imageToString(
            @RequestPart("image")MultipartFile multipartFile
            ) {
        try {
            return this.tesseractService.imageToString(multipartFile);
        } catch (IOException e) {
            return "error";
        }
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<AnalysisDTO> imageToStringByImageId(
            @PathVariable("imageId") Long id
    ) throws IOException {
        ImageEntity image = this.imageRepository.findById(id)
                .orElseThrow(CustomException::new);
        System.out.println("asdasdasdasdsa");
        return ResponseEntity.status(HttpStatus.OK).body(this.analysisService.analysisImage(id));
    }

}
