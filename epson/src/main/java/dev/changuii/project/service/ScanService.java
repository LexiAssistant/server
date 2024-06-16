package dev.changuii.project.service;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface ScanService {

    public Mono<Boolean> registerDestination(String email);

    public void uploadScanData(List<MultipartFile> files, String email) throws IOException;

    public byte[] downloadScanData(Long id);
    public List<String> downloadUserScanData(String email);

}
