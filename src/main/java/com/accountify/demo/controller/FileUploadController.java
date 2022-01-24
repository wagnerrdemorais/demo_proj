package com.accountify.demo.controller;

import com.accountify.demo.service.FileService;
import net.lingala.zip4j.progress.ProgressMonitor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

@Controller
@RestController("/")
public class FileUploadController {

    private final FileService fileService;

    public FileUploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<String> submit(@RequestParam("file") MultipartFile file) throws IOException {
        File newFile = fileService.getNewFile(file);
        Long checkSum = fileService.checksumGenerator(newFile);
        return ResponseEntity.ok("Path: " + newFile.getAbsolutePath() + " CheckSum: " + checkSum);
    }

    @RequestMapping(value = "/zipFile", method = RequestMethod.GET)
    public ResponseEntity<String> retrieve(@RequestParam("file") String file, @RequestParam("name") String name) throws IOException {
        fileService.zipTest(file, name);
        return ResponseEntity.ok("ok test");
    }

    @RequestMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, value = "/files", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getFile(@RequestParam("fileName") String fileName) {
        return new FileSystemResource(fileName);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/split")
    public ResponseBodyEmitter splitFile(@RequestParam("file") MultipartFile file) throws IOException {

        ProgressMonitor progressMonitor = fileService.zipSplitFileWithProgress(file);

        final ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                while (!progressMonitor.getState().equals(ProgressMonitor.State.READY)) {
                    emitter.send("Percentage done: " + progressMonitor.getPercentDone());
                    emitter.send(" - ", MediaType.TEXT_PLAIN);
                }
            } catch (Exception e) {
                e.printStackTrace();
                emitter.completeWithError(e);
                return;
            }
            emitter.complete();
        });

        return emitter;
    }

}
