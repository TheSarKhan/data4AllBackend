package org.example.dataprotal.model.analytics;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "analytics")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Analytic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String coverImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "sub_title_id",
            nullable = false
    )
    @JsonBackReference
    private SubTitle subTitle;

    @Column(name = "is_opened")
    private boolean isOpened;

    @OneToMany(
            mappedBy = "analytic",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    @JsonManagedReference
    private List<EmbedLink> embedLinks = new ArrayList<>();
}