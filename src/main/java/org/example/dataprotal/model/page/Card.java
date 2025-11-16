package org.example.dataprotal.model.page;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String author;
    String pageName;
    String title;
    int likes=0;
    int views=0;

    @Column(columnDefinition = "TEXT")
    String text;

    @JdbcTypeCode(SqlTypes.JSON)
    List<SubContent> subContents;

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime createdAt;
}
