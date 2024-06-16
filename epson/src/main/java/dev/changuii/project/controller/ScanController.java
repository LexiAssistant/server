package dev.changuii.project.controller;


import dev.changuii.project.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/scan")
public class ScanController {

    private final ScanService scanService;

    public ScanController(
            @Autowired ScanService scanService
    ){
        this.scanService = scanService;
    }


    @PostMapping("/regi/{email}")
    public Mono<ResponseEntity<Boolean>> authenticationAndRegistration(
            @PathVariable("email") String email
    ){
        return this.scanService.registerDestination(email)
                .map(success -> {
                    if(success) return ResponseEntity.status(HttpStatus.CREATED).body(success);
                    else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(success);
                });
    }

    @PostMapping("/{email}")
    public ResponseEntity<?> uploadScanData(
            @RequestPart("0") List<MultipartFile> files,
            @PathVariable("email") String email
    ) throws IOException {
        this.scanService.uploadScanData(files, email);
        return ResponseEntity.status(201).body(null);
    }


}
