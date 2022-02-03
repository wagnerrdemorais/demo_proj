package com.demo.controller;

import com.demo.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;


@Controller
public class FileDownloadController {

    private final FileService fileService;

    public FileDownloadController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/downloads")
    public String downloads(Model model) throws IOException {

        List<String> listOfPathFiles = fileService.listFilesPath();
        model.addAttribute("paths", listOfPathFiles);

        return "downloads";
    }

}
