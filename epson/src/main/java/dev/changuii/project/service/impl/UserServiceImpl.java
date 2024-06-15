package dev.changuii.project.service.impl;


import dev.changuii.project.dto.TokenPairResponseDTO;
import dev.changuii.project.dto.UserDTO;
import dev.changuii.project.entity.UserEntity;
import dev.changuii.project.repository.UserRepository;
import dev.changuii.project.security.service.JwtProvider;
import dev.changuii.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


    @Override
    public TokenPairResponseDTO signup(UserDTO userDTO) {

        if(!userRepository.existsByEmail(userDTO.getEmail()))
        {
            UserEntity user = userDTO.toUserEntity();
            userRepository.save(user);

            return new TokenPairResponseDTO(
                    jwtProvider.createAccessToken(user.getUserPK().toString()),
                    jwtProvider.createRefreshToken(user.getUserPK().toString()),
                    "OK"
            );
        }

        return new TokenPairResponseDTO(
                "","","Already exist Email"
        );

    }

    @Override
    public TokenPairResponseDTO login(UserDTO userDTO) {
        try {
            UserEntity user = userRepository.findByEmail(userDTO.getEmail());
            if(user.getPassword().equals(userDTO.getPassword())
                    && user.getPassword().equals(userDTO.getPassword())){
                return new TokenPairResponseDTO(
                        jwtProvider.createAccessToken(user.getUserPK().toString()),
                        jwtProvider.createRefreshToken(user.getUserPK().toString()),
                        "OK");
            }
        }catch (Exception e){
            //TODO: 예외정의
        }
        return null;
    }
}
