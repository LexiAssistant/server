package dev.changuii.project.repository;

import dev.changuii.project.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, CustomUserRepository {

    UserEntity findByEmail(String email);
    Boolean existsByEmail(String email);
}
