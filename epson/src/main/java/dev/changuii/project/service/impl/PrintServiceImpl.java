package dev.changuii.project.service.impl;

import dev.changuii.project.dto.PrintSettingDTO;
import dev.changuii.project.entity.UserEntity;
import dev.changuii.project.exception.CustomException;
import dev.changuii.project.repository.UserRepository;
import dev.changuii.project.service.PrintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;


@Service
public class PrintServiceImpl implements PrintService {

    private final UserRepository userRepository;
    Logger log = LoggerFactory.getLogger(PrintServiceImpl.class);

    private final WebClient webClient;

    public PrintServiceImpl(@Autowired WebClient.Builder webClient, UserRepository userRepository) {
        
        this. webClient = webClient.build();
        this.userRepository = userRepository;
    }

    private final String base = "https://api.epsonconnect.com/api/1";


    // 디바이스의 프린터 용량 체크
    @Override
    public Mono<Boolean> getDevicePrintCapability(String email)
    {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(CustomException::new);
        log.info(user.toString());
        String deviceId = user.getDeviceId();
        String token = user.getEpsonToken();

        String printType = "document";

        return webClient.get()
                .uri(base+"/printing/printers/{deviceId}/capability/{printType}", deviceId, printType)// choose document or photo
                .header(HttpHeaders.AUTHORIZATION, token)
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
        log.info("============================ PRINT SETTING===================================");
        UserEntity user = userRepository.findByEmail(email).orElseThrow(CustomException::new);
        log.info(user.toString());

        String token = user.getEpsonToken();

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
                .uri(base+"/printing/printers/{deviceId}/jobs", deviceId)
                .header(HttpHeaders.AUTHORIZATION, token)
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


    @Override
    public Mono<Boolean> uploadFile(String uploadURL, MultipartFile file) throws IOException {
        log.info("============================ UPLOAD FILE===================================");
        String extension = file.getContentType().split("/")[1];
        String contentLength = String.valueOf(file.getSize());
        String filename = file.getOriginalFilename();

        log.info("================================= FILE INFO ========================================");
        log.info("\n extension: " + extension + "\n contentLength: " + contentLength + "\n filename: " + filename); ;
        log.info("================================= FILE INFO ========================================");


        StringTokenizer st = new StringTokenizer(uploadURL, "?");
        String url = st.nextToken();
        String key = st.nextToken().replace("Key=", "");

        log.info("======================URL CHECK========================\n");
        log.info(url); log.info(key);
        log.info("======================URL CHECK========================");

        DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
        DataBuffer dataBuffer = bufferFactory.wrap(file.getBytes());
        log.info(url + " " + key);
        log.info(filename.replace(".","") + " " + extension);
        return webClient.post()
                .uri(uploadURL + "&File="+"1"+"."+extension)
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(BodyInserters.fromDataBuffers(Mono.just(dataBuffer)))
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();
                    log.info(status.toString());
                    return response.bodyToMono(Void.class)
                            .flatMap(body -> {
                                log.info("Inside flatMap"); // 로그 추가
                                if (status.is2xxSuccessful()) {
                                    log.info("Upload completed successfully");
                                    return Mono.just(true);
                                } else {
                                    log.warn("Upload failed with status: " + status);
                                    return Mono.just(false);
                                }
                            });
                });
    }






    @Override
    //프린트 실행
    public Mono<Boolean> executePrint(String email, String jobId){
        log.info("============================ EXECUTE PRINT===================================");
        UserEntity user = userRepository.findByEmail(email).orElseThrow(CustomException::new);

        String token = user.getEpsonToken();
        String deviceId = user.getDeviceId();
        log.info(token);
        log.info(deviceId);
        return webClient
                .post()
                .uri(base+"/printing/printers"+deviceId+"/jobs/"+jobId+"/print")
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchangeToMono(response -> {
                            HttpStatusCode status = response.statusCode();
                            log.info(status.toString());
                            return response.bodyToMono(LinkedHashMap.class)
                                    .flatMap(body ->{
                                        if(status.is2xxSuccessful()){
                                            log.info("출력 시작");
                                            log.info(body.toString());
                                            return Mono.just(true);
                                        }
                                        else {
                                            log.info(body.toString());
                                            return Mono.just(false);} // TODO: 예외 던지도록 수정

                                    });
                });

    }


    // get print job information
    @Override
    public Mono<Boolean> getPrintJobInfo(String email, String jobId)
    {
        log.info("============================ PRINT JOB INFO ===============================");
        UserEntity user = userRepository.findByEmail(email).orElseThrow(CustomException::new);
        String token = user.getEpsonToken();
        String deviceId = user.getDeviceId();

        return webClient
                .get()
                .uri(base + "/printing/printers/{device_id}/jobs/{jobId}",deviceId ,jobId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();
                    log.info(status.toString());
                    return response.bodyToMono(String.class)
                            .flatMap(body ->{
                                if(status.is2xxSuccessful()){
                                    log.info("출력 시작");
                                    return Mono.just(true);
                                }
                                else {
                                    log.info(body.toString());
                                    return Mono.just(false);} // TODO: 예외 던지도록 수정

                            });
                });
    }


}
