package dev.changuii.project.service.impl;


import dev.changuii.project.dto.TokenPairResponseDTO;
import dev.changuii.project.dto.UserDTO;
import dev.changuii.project.entity.UserEntity;
import dev.changuii.project.repository.UserRepository;
import dev.changuii.project.security.service.JwtProvider;
import dev.changuii.project.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


    @Override
    @Transactional
    public ResponseEntity<TokenPairResponseDTO> signup(UserDTO userDTO) {


        if(!userRepository.existsByEmail(userDTO.getEmail()))
        {
            log.info(userDTO.toString());
            UserEntity user = userDTO.toUserEntity();
            log.info(user.toString());
            userRepository.save(user);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new TokenPairResponseDTO(
                    jwtProvider.createAccessToken(user.getUserPK().toString()),
                    jwtProvider.createRefreshToken(user.getUserPK().toString())
                    ));
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new TokenPairResponseDTO());

    }

    @Override
    public ResponseEntity<TokenPairResponseDTO> login(UserDTO userDTO) {
        UserEntity user = userRepository.findByEmail(userDTO.getEmail());
        log.info(userDTO.toString());
        log.info(user.toString());
        if (user.getEmail().equals(userDTO.getEmail()) && user.getPassword().equals(userDTO.getPassword())){
            log.info("인증성공");

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new TokenPairResponseDTO(
                    jwtProvider.createAccessToken(user.getUserPK().toString()),
                    jwtProvider.createRefreshToken(user.getUserPK().toString())));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenPairResponseDTO());
    }

    @Override
    public boolean epsonAuthentication(String printerEmail) {
        return false;
    }

}