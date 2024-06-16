package dev.changuii.project.service;

import org.springframework.web.multipart.MultipartFile;

public interface ScanService {

    public MultipartFile authenticationAndRegisterDestination(String email);


}
