package org.example.dataprotal.model.analytics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "analytic_sub_titles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SubTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "title_id")
    @JsonIgnore
    private Title title;

    @Column(name = "is_opened")
    private boolean isOpened;

    @OneToMany(mappedBy = "subTitle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    @JsonManagedReference
    private List<Analytic> analytics = new ArrayList<>();
}
