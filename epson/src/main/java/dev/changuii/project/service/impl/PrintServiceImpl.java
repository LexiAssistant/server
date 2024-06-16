package dev.changuii.project.service.impl;

import dev.changuii.project.dto.PrintSettingDTO;
import dev.changuii.project.entity.UserEntity;
import dev.changuii.project.exception.CustomException;
import dev.changuii.project.repository.UserRepository;
import dev.changuii.project.service.PrintService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MimeType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.MediaSize;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


@Service
public class PrintServiceImpl implements PrintService {

    private final UserRepository userRepository;
    Logger log = LoggerFactory.getLogger(PrintServiceImpl.class);

    private final WebClient webClient;

    public PrintServiceImpl(@Autowired WebClient.Builder webClient, UserRepository userRepository) {
        
        this. webClient = webClient.baseUrl("https://api.epsonconnect.com/api/1").build();
        this.userRepository = userRepository;
    }


    // 디바이스의 프린터 용량 체크
    @Override
    public Mono<Boolean> getDevicePrintCapability(String email)
    {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(CustomException::new);
        log.info(user.toString());
        String deviceId = user.getDeviceId();

        String[] words = user.getEpsonToken().split(" ");

        String tokenType = words[0];
        String token = words[1];

        String printType = "document";
//        81cbb3f5ff6749e3948ba62e0dfcae2a
//        eroHCNqhq1q6dyociTcPOov2BYuXOyEH04K3VWfDOguYGRD9iIjZknMaW1Duw9dA
        return webClient.get()
//                .uri("/printing/printers/{deviceId}/capability/{print_mode}",deviceId, printType)
                .uri("/printing/printers/{deviceId}/capability/{printType}", deviceId, printType)
                // choose document or photo
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();
                    log.info(status.toString());
                    return response.bodyToMono(LinkedHashMap.class)
                            .flatMap(body -> {
                                log.info(body.toString());
                                if(status.is2xxSuccessful()){
                                    log.info("디바이스 저장공간 확인 완료");
                                    return Mono.just(true);
                                }
                                else return Mono.just(false);
                            });
                });

    }


    //프린트 세팅
    @Override
    public Mono<List<String>> printSetting(String email)
    {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(CustomException::new);
        log.info(user.toString());

        String[] words = user.getEpsonToken().split(" ");
        String tokenType = words[0];
        String token = words[1];

        String deviceId = user.getDeviceId();
        PrintSettingDTO printSettingDTO = new PrintSettingDTO();
        printSettingDTO.setJob_name("sample");
        printSettingDTO.setPrint_mode("photo");
        Map<String, Object> printSetting = Map.of(
                "media_size", "ms_a4",
                "media_type", "mt_plainpaper",
                "borderless", false,
                "print_quality", "normal",
                "source", "front1",
                "color_mode", "mono",
                "2_sided", "none",
                "reverse_order", false,
                "copies", 1,
                "collate", false
        );

        printSettingDTO.setPrint_setting(printSetting);


        return webClient.post()
                .uri("/printing/printers/{deviceId}/jobs", deviceId)
                .header(HttpHeaders.AUTHORIZATION,"Bearer "+ token)
                .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .bodyValue(printSettingDTO)
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();
                    log.info(status.toString());
                    return response.bodyToMono(LinkedHashMap.class)
                            .flatMap(body ->{
                                if(status.is2xxSuccessful()){
                                    String id = (String) body.get("id");
                                    String url = (String) body.get("upload_uri");
                                    List<String> result = Arrays.asList(id, url);
                                    log.info(result.toString());
                                    return Mono.just(result);
                                }
                                else return Mono.just(Collections.emptyList()); // TODO: 예외 던지도록 수정
                            });
                });
    }

    public Mono<Boolean> uploadFile(String uploadURL, MultipartFile file) {
        String extension = "jpeg";
        String contentLength = String.valueOf(file.getSize());
        String contentType = file.getContentType();

        ByteArrayResource resource;
        try {
            resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
        } catch (IOException e) {
            return Mono.error(e);
        }

        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("1.jpeg", resource);

        log.info("======================URL CHECK========================");
        log.info(uploadURL);

        return webClient.post()
                .uri(uploadURL)
                .header(HttpHeaders.CONTENT_LENGTH, contentLength)
//                .contentType(MediaType.IMAGE_JPEG)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(BodyInserters.fromMultipartData(formData))
                .retrieve()
                .bodyToMono(LinkedHashMap.class)
                .flatMap(body -> {
                    Integer status = (Integer) body.get("status");
                    log.info(status.toString());
                    if (status != null && status >= 200 && status < 300) {
                        return Mono.just(true);
                    } else {
                        return Mono.just(false);
                    }
                });
    }


    private static class MultipartFileResource extends ByteArrayResource {
        private final String filename;

        public MultipartFileResource(MultipartFile file) {
            super(getBytes(file));
            this.filename = file.getOriginalFilename();
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        private static byte[] getBytes(MultipartFile file) {
            try {
                return file.getBytes();
            } catch (IOException e) {
                throw new RuntimeException("Failed to read file bytes", e);
            }
        }
    }





    //프린트 실행
    public Mono<Boolean> excutePrint(String jobId, String email){
        UserEntity user = userRepository.findByEmail(email).orElseThrow(CustomException::new);

        String[] words = user.getEpsonToken().split(" ");
        String tokenType = words[0];
        String token = words[1];
        String deviceId = user.getDeviceId();

        return webClient.post().uri("printing/printers/{deviceId}/jobs/{jobId}", deviceId, jobId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchangeToMono(response -> {
                            HttpStatusCode status = response.statusCode();
                            log.info(status.toString());
                            return response.bodyToMono(LinkedHashMap.class)
                                    .flatMap(body ->{
                                        if(status.is2xxSuccessful()){
                                            log.info("출력 시작");
                                            return Mono.just(true);
                                        }
                                        else return Mono.just(false); // TODO: 예외 던지도록 수정
                                    });
                });
    }


    // get print job information


}
