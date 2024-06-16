package dev.changuii.project.service;

import dev.changuii.project.dto.TokenPairResponseDTO;
import dev.changuii.project.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface UserService {

    TokenPairResponseDTO signup(UserDTO userDTO);
    TokenPairResponseDTO login(UserDTO userDTO);

    public Mono<Boolean> epsonAuthentication(String printerEmail, String email);
}
