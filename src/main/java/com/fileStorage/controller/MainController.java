package com.fileStorage.controller;

import com.fileStorage.model.File;
import com.fileStorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    private final FileService fileService;

    @Autowired
    public MainController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public String main(Model model) {

        List<File> files = fileService.findAllFiles();
        model.addAttribute("files", files);
        return "index";
    }

}
