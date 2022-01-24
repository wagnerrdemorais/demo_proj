package com.accountify.demo.controller;

import com.accountify.demo.service.LancamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
public class QueryController {

    private final LancamentoService lancamentoService;

    public QueryController(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public ResponseEntity<List<String>> submit(@RequestBody String query) {
        return ResponseEntity.ok(lancamentoService.executeQuery(query));
    }
}
