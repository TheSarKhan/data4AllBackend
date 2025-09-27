package org.example.dataprotal.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSetQueryDto {
    String fullName;
    String email;
    String phoneNumber;
    String organization;
    String dataSetName;
    String purpose;
    @Column(columnDefinition = "TEXT")
    String notes;
    Boolean isRobot;
}
