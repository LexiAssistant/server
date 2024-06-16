package dev.changuii.project.controller;


import dev.changuii.project.dto.TokenPairResponseDTO;
import dev.changuii.project.dto.UserDTO;
import dev.changuii.project.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping(value = "/signin")
    public ResponseEntity<TokenPairResponseDTO> signin(@RequestBody UserDTO userDTO) {
        log.info(userDTO.toString());
        return userServiceImpl.login(userDTO);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<TokenPairResponseDTO> signup(@RequestBody UserDTO userDTO) {
        log.info(userDTO.toString());
        return userServiceImpl.signup(userDTO);
    }


    @PostMapping("/epson/{printerEmail}/{email}")
    public ResponseEntity<Boolean> epsonAuthentication(
            @PathVariable("printerEmail") String printerEmail,
            @PathVariable("email") String email
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userServiceImpl.epsonAuthentication(printerEmail, email))
    }

}
