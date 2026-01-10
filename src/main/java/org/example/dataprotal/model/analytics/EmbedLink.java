package org.example.dataprotal.model.analytics;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "embed_links")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmbedLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String embedLink;

    @ManyToOne
    @JoinColumn(name = "analytic_id", nullable = false)
    @JsonBackReference
    private Analytic analytic;
}
