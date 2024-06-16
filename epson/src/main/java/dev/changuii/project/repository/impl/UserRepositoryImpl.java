package dev.changuii.project.repository.impl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryImpl {
    private JPAQueryFactory jpaQueryFactory;


    public UserRepositoryImpl(
            @Autowired JPAQueryFactory jpaQueryFactory
    ){
        this.jpaQueryFactory = jpaQueryFactory;
    }


}
