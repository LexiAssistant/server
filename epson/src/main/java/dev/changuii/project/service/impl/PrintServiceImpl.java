package dev.changuii.project.service.impl;

import dev.changuii.project.service.PrintSetvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class PrintServiceImpl implements PrintSetvice {

    private final WebClient webClient;

    public PrintServiceImpl(@Autowired WebClient webClient) {

        this. webClient = WebClient.builder().baseUrl("https://api.epsonconnect.com/api/1").build();
    }


}
