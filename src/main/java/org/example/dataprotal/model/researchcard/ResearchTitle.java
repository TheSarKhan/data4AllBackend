package org.example.dataprotal.model.researchcard;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "research_titles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResearchTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private boolean isOpened;

    @OneToMany(mappedBy = "title", fetch = FetchType.EAGER)
    @Builder.Default
    @JsonManagedReference
    private List<ResearchSubTitle> subTitles = new ArrayList<>();
}
