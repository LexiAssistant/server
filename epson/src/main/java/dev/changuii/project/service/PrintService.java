package dev.changuii.project.service;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface PrintService {

    Mono<Boolean> getDevicePrintCapability(String email);

    Mono<List<String>> printSetting(String email);
    Mono<Boolean> uploadFile(String uploadURL, MultipartFile file) throws IOException;
    Mono<Boolean> executePrint(String email, String jobId);
    Mono<Boolean> getPrintJobInfo(String email, String jobId);
}
