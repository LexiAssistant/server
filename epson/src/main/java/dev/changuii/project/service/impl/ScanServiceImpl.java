package dev.changuii.project.service.impl;

import dev.changuii.project.entity.ImageEntity;
import dev.changuii.project.entity.UserEntity;
import dev.changuii.project.exception.CustomException;
import dev.changuii.project.repository.ImageRepository;
import dev.changuii.project.repository.UserRepository;
import dev.changuii.project.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;


@Service
public class ScanServiceImpl implements ScanService {

    private final WebClient webClient;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Value("${server.host-name}")
    private String host;

    public ScanServiceImpl(
            @Autowired ImageRepository imageRepository,
            @Autowired WebClient.Builder webClientBuilder,
            @Autowired UserRepository userRepository
    ){
        this.webClient = webClientBuilder.baseUrl("https://api.epsonconnect.com/api/1").build();
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }


    @Override
    public Mono<Boolean> registerDestination(String email) {

        // 스캔 목적지
        LinkedHashMap<String, String> scanDestination;
        scanDestination = new LinkedHashMap<>();
        scanDestination.put("alias_name", "lexiAssistance");
        scanDestination.put("type", "url");
        scanDestination.put("destination", host + "/email");

        // 인증 토큰 가져오기
        UserEntity user = this.userRepository.findByEmail(email)
                .orElseThrow(CustomException::new);

        String token = user.getEpsonToken();
        String deviceId = user.getDeviceId();
        // 스캔 목적지 등록
        return webClient.post()
                .uri("/scanning/scanners/" + deviceId + "/destinations")
                .header(HttpHeaders.AUTHORIZATION, token)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(scanDestination))
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();
                    return response.bodyToMono(LinkedHashMap.class)
                            .flatMap(res -> {
                                if(res.containsValue("code")){
                                    if(((String) res.get("code")).equals("duplicate_alias_error")) return Mono.just(true);
                                    return Mono.just(false);
                                }else return Mono.just(true);
                            });
                });
    }

    @Override
    public void uploadScanData(List<MultipartFile> files, String email) throws IOException {
        UserEntity user = this.userRepository.findByEmail(email)
                .orElseThrow(CustomException::new);

        for(MultipartFile f : files){
            ImageEntity image = ImageEntity.builder().image(f.getBytes()).user(user).build();
            this.imageRepository.save(image);
        }
    }
}
