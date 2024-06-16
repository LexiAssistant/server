package dev.changuii.project.service.impl;


import dev.changuii.project.dto.AccessTokenResponseDTO;
import dev.changuii.project.dto.TokenPairResponseDTO;
import dev.changuii.project.dto.UserDTO;
import dev.changuii.project.entity.UserEntity;
import dev.changuii.project.exception.CustomException;
import dev.changuii.project.repository.UserRepository;
import dev.changuii.project.security.service.JwtProvider;
import dev.changuii.project.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;

@Service
public class UserServiceImpl implements UserService {

    Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final WebClient webClient;

    @Value("${apson.api-key}")
    private String key;
    public UserServiceImpl(
            @Autowired UserRepository userRepository,
            @Autowired JwtProvider jwtProvider,
            @Autowired WebClient.Builder webClientBuilder
    ){
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.webClient = webClientBuilder.baseUrl("https://api.epsonconnect.com/api/1").build();
    }




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
        UserEntity user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(CustomException::new);

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
    public ResponseEntity<AccessTokenResponseDTO> issueAccessToken(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow(CustomException::new);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new AccessTokenResponseDTO(
                        jwtProvider.createAccessToken(user.getUserPK().toString())
                ));
    }


    @Override
    public Mono<Boolean> epsonAuthentication(String printerEmail, String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(CustomException::new);

        return webClient.post()
                .uri("/printing/oauth2/auth/token?subject=printer")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", key )
                .body(BodyInserters.fromFormData("password", "")
                        .with("grant_type", "password").with("username", email))
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();
                    return response.bodyToMono(LinkedHashMap.class)
                            .flatMap(body -> {
                                if(status.is2xxSuccessful()){
                                    String token = (String) body.get("access_token");
                                    String deviceId = (String) body.get("subject_id");
                                    this.userRepository.save(UserEntity.epsonAuthenticationStore(user, token, deviceId));
                                    return Mono.just(true);
                                }
                                else return Mono.just(false);
                            });
                });
    }

}