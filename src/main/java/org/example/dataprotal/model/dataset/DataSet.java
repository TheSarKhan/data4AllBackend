package org.example.dataprotal.model.dataset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dataprotal.model.enums.DataSetStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "datasets")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String author;

    String dataSetName;

    String title;

    @Column(columnDefinition = "TEXT")
    String description;

    String imageUrl;

    String fileUrl;

    @Column(name = "is_opened")
    boolean isOpened;

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    DataSetCategory category;

    @ManyToOne
    @JoinColumn(name = "intern_id")
    @JsonIgnoreProperties("datasets")
    Intern intern;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    DataSetStatus status;

    @PrePersist
    public void prePersist() {
        status = DataSetStatus.PENDING;
    }
}
