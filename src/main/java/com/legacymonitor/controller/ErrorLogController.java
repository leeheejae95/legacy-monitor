package com.legacymonitor.controller;

import com.legacymonitor.domain.ErrorLog;
import com.legacymonitor.repository.ErrorLogRepository;
import com.legacymonitor.util.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/error-log")
@RequiredArgsConstructor
public class ErrorLogController {

    private final ErrorLogRepository errorLogRepository;

    @GetMapping
    public String list(Model model, Principal principal) {
        List<ErrorLog> logs = errorLogRepository.findAll();
        int totalCount     = logs.stream().mapToInt(ErrorLog::getCount).sum();
        long serviceCount  = logs.stream().map(ErrorLog::getServiceName).distinct().count();
        int maxCount       = logs.stream().mapToInt(ErrorLog::getCount).max().orElse(0);

        model.addAttribute("logs", logs);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("serviceCount", serviceCount);
        model.addAttribute("maxCount", maxCount);
        model.addAttribute("username", principal.getName());
        return "error-log";
    }

    @GetMapping("/excel")
    public void excel(HttpServletResponse response) throws IOException {
        List<ErrorLog> logs = errorLogRepository.findAll();
        String[] headers = {"No", "서비스명", "에러타입", "발생시간", "횟수"};
        List<Object[]> rows = logs.stream()
                .map(l -> new Object[]{
                        logs.indexOf(l) + 1,
                        l.getServiceName(),
                        l.getErrorType(),
                        l.getOccurredAt().toString(),
                        l.getCount()
                })
                .collect(Collectors.toList());

        Workbook wb = ExcelUtil.createWorkbook("시스템에러로그", headers, rows);
        ExcelUtil.write(response, wb, "시스템에러로그.xlsx");
    }
}