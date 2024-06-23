package dev.changuii.project.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class ImageEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image")
    @Lob
    private byte[] image;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
