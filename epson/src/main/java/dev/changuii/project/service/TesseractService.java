package dev.changuii.project.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TesseractService {

        public String imageToString(MultipartFile mfile) throws IOException;

}
