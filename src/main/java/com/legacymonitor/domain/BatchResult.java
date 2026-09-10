package com.legacymonitor.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "batch_result")
@Data
@NoArgsConstructor
public class BatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_date")
    private LocalDate batchDate; // 날짜

    @Column(name = "batch_name")
    private String batchName; // 배치명

    @Column(name = "total_count")
    private int totalCount; // 처리건수

    @Column(name = "success_count")
    private int successCount; // 성공

    @Column(name = "fail_count")
    private int failCount; // 실패

    private String status; // 상태 (정상 / 오류)
}