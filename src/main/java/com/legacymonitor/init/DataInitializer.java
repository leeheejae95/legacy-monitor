package com.legacymonitor.init;

import com.legacymonitor.domain.BatchResult;
import com.legacymonitor.domain.CivilComplaint;
import com.legacymonitor.domain.ErrorLog;
import com.legacymonitor.repository.BatchResultRepository;
import com.legacymonitor.repository.CivilComplaintRepository;
import com.legacymonitor.repository.ErrorLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BatchResultRepository batchResultRepository;
    private final CivilComplaintRepository civilComplaintRepository;
    private final ErrorLogRepository errorLogRepository;

    @Override
    public void run(String... args) {
        initBatchResults();
        initCivilComplaints();
        initErrorLogs();
    }

    private void initBatchResults() {
        String[] names = {
            "급여정산배치", "민원통계배치", "세금계산배치", "인허가집계배치", "문서백업배치",
            "사용자동기화배치", "통계집계배치", "알림발송배치", "만료처리배치", "정산확정배치"
        };
        String[] statuses = {
            "정상", "정상", "오류", "정상", "정상",
            "정상", "오류", "정상", "오류", "정상",
            "정상", "정상", "오류", "정상", "정상",
            "오류", "정상", "정상", "정상", "오류"
        };

        LocalDate base = LocalDate.now().minusDays(19);
        for (int i = 0; i < 20; i++) {
            BatchResult b = new BatchResult();
            b.setBatchDate(base.plusDays(i));
            b.setBatchName(names[i % names.length]);
            int total = 100 + (i * 47 + 300) % 900;
            boolean isNormal = "정상".equals(statuses[i]);
            int fail = isNormal ? 0 : (5 + (i * 3) % 20);
            b.setTotalCount(total);
            b.setSuccessCount(total - fail);
            b.setFailCount(fail);
            b.setStatus(statuses[i]);
            batchResultRepository.save(b);
        }
    }

    private void initCivilComplaints() {
        String[] types = {
            "건축허가", "토지이용확인", "사업자등록", "인허가신청", "영업신고",
            "폐업신고", "주소변경", "차량등록", "재산세조회", "민원확인서발급"
        };
        String[] statuses = {
            "완료", "처리중", "접수", "완료", "반려",
            "완료", "처리중", "완료", "접수", "완료",
            "반려", "완료", "처리중", "완료", "접수",
            "완료", "완료", "반려", "처리중", "완료"
        };

        LocalDate base = LocalDate.now().minusDays(57);
        for (int i = 0; i < 20; i++) {
            CivilComplaint c = new CivilComplaint();
            c.setReceiptNumber(String.format("2025-민원-%04d", i + 1));
            c.setComplaintType(types[i % types.length]);
            c.setReceiptDate(base.plusDays(i * 3));
            c.setStatus(statuses[i]);
            civilComplaintRepository.save(c);
        }
    }

    private void initErrorLogs() {
        String[] services = {
            "민원처리시스템", "급여처리서비스", "문서관리시스템", "인허가서비스", "통계집계서비스",
            "알림발송서비스", "사용자인증서비스", "파일저장서비스", "배치스케줄러", "API게이트웨이"
        };
        String[] errorTypes = {
            "NullPointerException", "TimeoutException", "ConnectionRefusedException",
            "DataIntegrityViolationException", "IllegalArgumentException",
            "OutOfMemoryError", "FileNotFoundException", "AuthenticationException"
        };

        LocalDateTime base = LocalDateTime.now().minusHours(76);
        for (int i = 0; i < 20; i++) {
            ErrorLog e = new ErrorLog();
            e.setServiceName(services[i % services.length]);
            e.setErrorType(errorTypes[i % errorTypes.length]);
            e.setOccurredAt(base.plusHours(i * 4 + i % 5).plusMinutes(i * 13 % 60));
            e.setCount(1 + (i * 7) % 30);
            errorLogRepository.save(e);
        }
    }
}