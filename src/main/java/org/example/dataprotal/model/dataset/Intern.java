package org.example.dataprotal.model.dataset;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dataprotal.model.enums.PositionStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interns")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Intern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;

    private PositionStatus position;

    @OneToMany(mappedBy = "intern", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Builder.Default
    private List<DataSet> datasets=new ArrayList<>();
}
