package com.legacymonitor.controller;

import com.legacymonitor.domain.BatchResult;
import com.legacymonitor.repository.BatchResultRepository;
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
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {

    private final BatchResultRepository batchResultRepository;

    @GetMapping
    public String list(Model model, Principal principal) {
        List<BatchResult> results = batchResultRepository.findAll();
        long normalCount = results.stream().filter(r -> "정상".equals(r.getStatus())).count();
        long errorCount  = results.size() - normalCount;

        model.addAttribute("results", results);
        model.addAttribute("normalCount", normalCount);
        model.addAttribute("errorCount", errorCount);
        model.addAttribute("username", principal.getName());
        return "batch";
    }

    @GetMapping("/excel")
    public void excel(HttpServletResponse response) throws IOException {
        List<BatchResult> results = batchResultRepository.findAll();
        String[] headers = {"No", "날짜", "배치명", "처리건수", "성공", "실패", "상태"};
        List<Object[]> rows = results.stream()
                .map(r -> new Object[]{
                        results.indexOf(r) + 1,
                        r.getBatchDate().toString(),
                        r.getBatchName(),
                        r.getTotalCount(),
                        r.getSuccessCount(),
                        r.getFailCount(),
                        r.getStatus()
                })
                .collect(Collectors.toList());

        Workbook wb = ExcelUtil.createWorkbook("배치처리결과", headers, rows);
        ExcelUtil.write(response, wb, "배치처리결과.xlsx");
    }
}