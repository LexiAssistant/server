package dev.changuii.project.service;

import dev.changuii.project.dto.TokenPairResponseDTO;
import dev.changuii.project.dto.UserDTO;

public interface UserService {

    TokenPairResponseDTO signup(UserDTO userDTO);
    TokenPairResponseDTO login(UserDTO userDTO);

    public boolean epsonAuthentication(String printerEmail);
}
