package dev.changuii.project.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ImageEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image")
    private byte[] image;

    @ManyToOne
    @JoinColumn
    private UserEntity user;
}
