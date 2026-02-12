package org.example.dataprotal.model.dataset;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "dataset_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataSetCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    private String iconUrl;

    @Column(name = "is_opened")
    private boolean isOpened;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<DataSet> datasets;
}
