package dev.changuii.project.service;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface PrintService {

    Mono<String> getDevicePrintCapability(String email, String printType);

    Mono<List<String>> printSetting(String email, String printType);
    Mono<Boolean> uploadFile(String uploadURL, MultipartFile file) throws IOException;
    Mono<Boolean> executePrint(String email, String jobId);
    Mono<String> getPrintJobInfo(String email, String jobId);
}
