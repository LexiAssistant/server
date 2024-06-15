package dev.changuii.project.controller;


import dev.changuii.project.service.TesseractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/sample")
public class SampleController {


    private TesseractService tesseractService;

    public SampleController(
            @Autowired TesseractService tesseractService
    ){
        this.tesseractService = tesseractService;
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

}
