package com.repo_generator.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class SystemController {

    @GetMapping
    public String index() {
        return "index";
    }
}
