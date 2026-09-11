package com.legacymonitor.controller;

import com.legacymonitor.domain.CivilComplaint;
import com.legacymonitor.repository.CivilComplaintRepository;
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
@RequestMapping("/civil")
@RequiredArgsConstructor
public class CivilController {

    private final CivilComplaintRepository civilComplaintRepository;

    @GetMapping
    public String list(Model model, Principal principal) {
        List<CivilComplaint> complaints = civilComplaintRepository.findAll();
        long completedCount = complaints.stream().filter(c -> "완료".equals(c.getStatus())).count();
        long pendingCount   = complaints.stream().filter(c -> "처리중".equals(c.getStatus()) || "접수".equals(c.getStatus())).count();
        long rejectedCount  = complaints.stream().filter(c -> "반려".equals(c.getStatus())).count();

        model.addAttribute("complaints", complaints);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("rejectedCount", rejectedCount);
        model.addAttribute("username", principal.getName());
        return "civil";
    }

    @GetMapping("/excel")
    public void excel(HttpServletResponse response) throws IOException {
        List<CivilComplaint> complaints = civilComplaintRepository.findAll();
        String[] headers = {"No", "접수번호", "민원종류", "접수일", "상태"};
        List<Object[]> rows = complaints.stream()
                .map(c -> new Object[]{
                        complaints.indexOf(c) + 1,
                        c.getReceiptNumber(),
                        c.getComplaintType(),
                        c.getReceiptDate().toString(),
                        c.getStatus()
                })
                .collect(Collectors.toList());

        Workbook wb = ExcelUtil.createWorkbook("민원처리현황", headers, rows);
        ExcelUtil.write(response, wb, "민원처리현황.xlsx");
    }
}