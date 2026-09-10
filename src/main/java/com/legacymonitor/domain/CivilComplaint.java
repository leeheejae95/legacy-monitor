package com.legacymonitor.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "civil_complaint")
@Data
@NoArgsConstructor
public class CivilComplaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receipt_number")
    private String receiptNumber; // 접수번호

    @Column(name = "complaint_type")
    private String complaintType; // 민원종류

    @Column(name = "receipt_date")
    private LocalDate receiptDate; // 접수일

    private String status; // 상태 (접수 / 처리중 / 완료 / 반려)
}