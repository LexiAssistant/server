package dev.changuii.project.service.impl;

import dev.changuii.project.entity.AnalysisDTO;
import dev.changuii.project.entity.ImageEntity;
import dev.changuii.project.exception.CustomException;
import dev.changuii.project.repository.ImageRepository;
import dev.changuii.project.service.AnalysisService;
import dev.changuii.project.service.TesseractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.LinkedHashMap;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    private final TesseractService tesseractService;
    private final ImageRepository imageRepository;
    private final WebClient webClient;
    private final static Logger log = LoggerFactory.getLogger(AnalysisServiceImpl.class);

    public AnalysisServiceImpl(
            @Autowired TesseractService tesseractService,
            @Autowired ImageRepository imageRepository,
            @Autowired WebClient.Builder webClientBuilder
    ){
        this.imageRepository=imageRepository;
        this.tesseractService=tesseractService;
        this.webClient = webClientBuilder.baseUrl("https://epson.n-e.kr:8000").build();
    }

    @Override
    public AnalysisDTO analysisImage(Long imageId) throws IOException {

        // tesseract로 문자열 생성
        ImageEntity image = this.imageRepository.findById(imageId)
                .orElseThrow(CustomException::new);
        String data = tesseractService.imageToString(image.getImage());

        // image service로 데이터 받아오기
        LinkedHashMap res = webClient.post()
                .uri("/process_text")
                .body(BodyInserters.fromValue(data))
                .exchangeToMono(clientResponse -> {
                    HttpStatusCode status = clientResponse.statusCode();
                    return clientResponse.bodyToMono(LinkedHashMap.class);
                }).block();
        // 반환
        log.info(res.get("keywords").toString());
        log.info(res.get("top_kewords").toString());
        log.info(res.get("summary").toString());

        return null;
    }
}
