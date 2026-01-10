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
    Long id;

    @Column(unique = true, nullable = false)
    String name;

    String description;

    String iconUrl;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    List<DataSet> datasets;
}
