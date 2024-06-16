package dev.changuii.project.controller;


import dev.changuii.project.dto.AccessTokenResponseDTO;
import dev.changuii.project.dto.TokenPairResponseDTO;
import dev.changuii.project.dto.UserDTO;
import dev.changuii.project.service.UserService;
import dev.changuii.project.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;



    @PostMapping(value = "/signin")
    public ResponseEntity<TokenPairResponseDTO> signin(@RequestBody UserDTO userDTO) {
        log.info(userDTO.toString());
        return userService.login(userDTO);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<TokenPairResponseDTO> signup(@RequestBody UserDTO userDTO) {
        log.info(userDTO.toString());
        return userService.signup(userDTO);
    }

    @GetMapping(value = "/{email}/issue/token")
    public ResponseEntity<AccessTokenResponseDTO> issueAccessToken(@PathVariable(name = "email") String email) {
        return userService.issueAccessToken(email);
    }


    @PostMapping("/epson/{printerEmail}/{email}")
    public ResponseEntity<Boolean> epsonAuthentication(
            @PathVariable("printerEmail") String printerEmail,
            @PathVariable("email") String email
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.epsonAuthentication(printerEmail, email));
    }




}
