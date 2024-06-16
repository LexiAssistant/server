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

    @Value("${apson.api-key}")
    private String key;

    public ScanServiceImpl(
            @Autowired WebClient.Builder webClientBuilder
    ){
        this.webClient = webClientBuilder.baseUrl("https://api.epsonconnect.com/api/1").build();
    }


    @Override
    public MultipartFile authenticationAndRegisterDestination(String email) {


        // 인증
        this.webClient.post()
                .uri("/printing/oauth2/auth/token?subject=printer")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", key )
                .body(BodyInserters.fromFormData("password", "")
                        .with("grant_type", "password").with("username", email))
                .retrieve()
                .bodyToMono(LinkedHashMap.class)
                .flatMap(authData->{
                        String token = (String) authData.get("access_token");
                        String deviceId = (String) authData.get("subject_id");

                });


        // 스캔 목적지 등록


        return null;
    }
}
