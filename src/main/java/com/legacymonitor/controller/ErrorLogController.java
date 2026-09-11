package com.legacymonitor.controller;

import com.legacymonitor.domain.ErrorLog;
import com.legacymonitor.repository.ErrorLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

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
}