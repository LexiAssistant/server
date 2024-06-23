package dev.changuii.project.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class ImageEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "keywords")
    @ElementCollection
    private List<String> keywords;

    @Column(name = "topKeyword")
    @ElementCollection
    private List<String> topKeywords;

    @Column(name = "summary")
    @ElementCollection
    private List<String> summary;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
