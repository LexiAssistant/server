package dev.changuii.project.service;

import dev.changuii.project.dto.AccessTokenResponseDTO;
import dev.changuii.project.dto.TokenPairResponseDTO;
import dev.changuii.project.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface UserService {

    ResponseEntity<TokenPairResponseDTO> signup(UserDTO userDTO);
    ResponseEntity<TokenPairResponseDTO> login(UserDTO userDTO);
    ResponseEntity<AccessTokenResponseDTO> issueAccessToken(String email);
    public Mono<Boolean> epsonAuthentication(String printerEmail, String email);
}
