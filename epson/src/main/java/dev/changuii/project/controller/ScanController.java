package dev.changuii.project.controller;


import dev.changuii.project.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scan")
public class ScanController {

    private final ScanService scanService;

    public ScanController(
            @Autowired ScanService scanService
    ){
        this.scanService = scanService;
    }


    @PostMapping("/auth-regi/{email}")
    public ResponseEntity<?> authenticationAndRegistration(
            @PathVariable("email") String email
    ){
        this.scanService.authenticationAndRegisterDestination(email);
    }


}
