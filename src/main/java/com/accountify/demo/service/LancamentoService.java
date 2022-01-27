package com.accountify.demo.service;

import com.accountify.demo.repository.LancamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;

    public LancamentoService(LancamentoRepository lancamentoRepository) {
        this.lancamentoRepository = lancamentoRepository;
    }

    public List<String> executeQuery(String query) {
        return lancamentoRepository.executeQuery(query);
    }
}
