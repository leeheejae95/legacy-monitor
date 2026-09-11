package com.legacymonitor.controller;

import com.legacymonitor.domain.CivilComplaint;
import com.legacymonitor.repository.CivilComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

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
}