package dev.changuii.project.entity;


import dev.changuii.project.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter @NoArgsConstructor @AllArgsConstructor
@ToString @Builder
public class UserEntity implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userPK;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "epson_token")
    private String epsonToken;

    @Column(name = "device_id")
    private String deviceId; //subjectId

    @OneToMany(mappedBy = "user") @Builder.Default
    private List<ImageEntity> images = new ArrayList<>();




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }


    public UserDTO toUserDTO(){
        return new UserDTO(this.email, this.password);
    }

    public UserEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserEntity epsonAuthenticationStore(UserEntity user, String epsonToken, String deviceId){
        return UserEntity.builder()
                .email(user.getEmail())
                .userPK(user.getUserPK())
                .password(user.getPassword())
                .epsonToken(epsonToken)
                .deviceId(deviceId).build();
    }


}
