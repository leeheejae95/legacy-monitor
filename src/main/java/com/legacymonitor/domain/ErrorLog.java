package com.legacymonitor.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "error_log")
@Data
@NoArgsConstructor
public class ErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name")
    private String serviceName; // 서비스명

    @Column(name = "error_type")
    private String errorType; // 에러타입

    @Column(name = "occurred_at")
    private LocalDateTime occurredAt; // 발생시간

    @Column(name = "error_count")
    private int count; // 횟수 (컬럼명: error_count, SQL 예약어 충돌 방지)
}