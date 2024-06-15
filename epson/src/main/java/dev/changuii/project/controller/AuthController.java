package dev.changuii.project.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping(value = "/signin")
    public ResponseEntity signin() {

        return ResponseEntity.status(HttpStatus.OK).body("흠");
    }

    @PostMapping(value = "/signup")
    public ResponseEntity signup() {
        return ResponseEntity.status(HttpStatus.OK).body("흠냐");
    }
}
