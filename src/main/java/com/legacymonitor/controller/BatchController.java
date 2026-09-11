package com.legacymonitor.controller;

import com.legacymonitor.domain.BatchResult;
import com.legacymonitor.repository.BatchResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

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
}