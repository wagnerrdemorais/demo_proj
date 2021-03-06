package com.demo.controller;

import com.demo.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class UploadControler {

    private final FileService fileService;
    Logger logger = LoggerFactory.getLogger(UploadControler.class);

    public UploadControler(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/upload")
    public String uploadPage() {
        logger.info("file_manager");
        return "file_manager";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        logger.info("uploadFile: file: " + file.getName());

        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload.");
            return "file_manager";
        }

        File newFile = null;
        try {
            newFile = fileService.getNewFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("message", "You successfully uploaded " + newFile.getName() + '!');
        return "file_manager";
    }
}
