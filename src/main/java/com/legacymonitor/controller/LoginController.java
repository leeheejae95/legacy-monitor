package com.legacymonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String root() {
        return "redirect:/batch";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}