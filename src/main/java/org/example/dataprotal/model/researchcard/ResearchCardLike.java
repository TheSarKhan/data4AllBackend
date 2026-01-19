package org.example.dataprotal.model.researchcard;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "research_card_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "research_card_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResearchCardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "research_card_id", nullable = false)
    private ResearchCard researchCard;

    private Long userId;
}
