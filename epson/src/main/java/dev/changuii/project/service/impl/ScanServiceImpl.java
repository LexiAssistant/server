package dev.changuii.project.service.impl;

import dev.changuii.project.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;


@Service
public class ScanServiceImpl implements ScanService {

    private final WebClient webClient;

    public ScanServiceImpl(
            @Autowired WebClient.Builder webClientBuilder
    ){
        this.webClient = webClientBuilder.baseUrl("https://api.epsonconnect.com/api/1").build();
    }


    @Override
    public MultipartFile authenticationAndRegisterDestination(String email) {


        // 인증


        // 스캔 목적지 등록


        return null;
    }
}
