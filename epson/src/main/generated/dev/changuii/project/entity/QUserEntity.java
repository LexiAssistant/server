package dev.changuii.project.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = 1055187951L;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final StringPath deviceId = createString("deviceId");

    public final StringPath email = createString("email");

    public final StringPath epsonToken = createString("epsonToken");

    public final ListPath<ImageEntity, QImageEntity> images = this.<ImageEntity, QImageEntity>createList("images", ImageEntity.class, QImageEntity.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final NumberPath<Long> userPK = createNumber("userPK", Long.class);

    public QUserEntity(String variable) {
        super(UserEntity.class, forVariable(variable));
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEntity(PathMetadata metadata) {
        super(UserEntity.class, metadata);
    }

}

