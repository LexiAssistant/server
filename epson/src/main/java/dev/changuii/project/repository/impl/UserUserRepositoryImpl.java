package dev.changuii.project.repository.impl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.changuii.project.repository.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static dev.changuii.project.entity.QUserEntity.userEntity;

public class UserUserRepositoryImpl implements CustomUserRepository {
    private JPAQueryFactory jpaQueryFactory;


    public UserUserRepositoryImpl(
            @Autowired JPAQueryFactory jpaQueryFactory
    ){
        this.jpaQueryFactory = jpaQueryFactory;
    }


}
