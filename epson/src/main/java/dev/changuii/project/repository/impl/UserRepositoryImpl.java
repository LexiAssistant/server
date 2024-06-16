package dev.changuii.project.repository.impl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.changuii.project.repository.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryImpl implements CustomUserRepository {
    private JPAQueryFactory jpaQueryFactory;


    public UserRepositoryImpl(
            @Autowired JPAQueryFactory jpaQueryFactory
    ){
        this.jpaQueryFactory = jpaQueryFactory;
    }


}
