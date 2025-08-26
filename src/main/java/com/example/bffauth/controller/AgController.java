package com.example.bffauth.controller;

import com.example.bffauth.service.AgService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ag")
public class AgController {

    private final AgService agService;

    public AgController(AgService agService) {
        this.agService = agService;
    }

    @GetMapping("/files")
    public String getFiles() {
        try {
            return agService.getFiles();
        } catch (Exception e) {
            e.printStackTrace();
            return "Hata oluştu: " + e.getMessage();
        }
    }
}

