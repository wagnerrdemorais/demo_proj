package com.demo.controller;

import com.demo.service.FileService;
import com.demo.service.LancamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class QueryController {

    private final FileService fileService;
    private final LancamentoService lancamentoService;

    public QueryController(FileService fileService, LancamentoService lancamentoService) {
        this.fileService = fileService;
        this.lancamentoService = lancamentoService;
    }

    @PostMapping("/query")
    public ResponseEntity<List<String>> execute(@RequestParam(name = "query") String query) {
        List<String> queryReturn = lancamentoService.executeQuery(query + " limit 10");
        return ResponseEntity.status(HttpStatus.OK).body(queryReturn);
    }

    @PostMapping("/queryToFile")
    public ResponseEntity<List<String>> executeToFile(@RequestParam(name = "query") String query) throws IOException {
        List<String> queryReturn = lancamentoService.executeQuery(query);
        fileService.createFile("queryResult", ".csv", queryReturn);
        return ResponseEntity.status(HttpStatus.OK).body(queryReturn);
    }

    @GetMapping("/query_executer")
    public String query() {
        return "query_executer";
    }
}
