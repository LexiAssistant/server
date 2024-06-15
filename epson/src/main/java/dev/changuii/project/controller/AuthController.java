package dev.changuii.project.controller;


import dev.changuii.project.dto.TokenPairResponseDTO;
import dev.changuii.project.dto.UserDTO;
import dev.changuii.project.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserServiceImpl userServiceImpl;

    public AuthController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<TokenPairResponseDTO> signin(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userServiceImpl.login(userDTO));
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<TokenPairResponseDTO> signup(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userServiceImpl.signup(userDTO));
    }

}
