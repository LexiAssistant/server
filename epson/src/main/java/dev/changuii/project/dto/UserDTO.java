package dev.changuii.project.dto;


import dev.changuii.project.entity.UserEntity;
import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @ToString
public class UserDTO {

    String email;
    String password;

    public UserEntity toUserEntity()
    {
        return new UserEntity( 0L,  this.email, this.password);
    }

}
