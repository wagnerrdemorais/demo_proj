package com.accountify.demo.controller;

import com.accountify.demo.service.LancamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class QueryController {

    private final LancamentoService lancamentoService;

    public QueryController(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    @PostMapping("/query")
    public ResponseEntity<List<String>> execute(@RequestParam(name = "query") String query) {
        List<String> queryReturn = lancamentoService.executeQuery(query);
        return ResponseEntity.status(HttpStatus.OK).body(queryReturn);
    }

    @GetMapping("/query_executer")
    public String query() {
        return "query_executer";
    }
}
