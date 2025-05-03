package com.dev.br.campeonato_brasileiro.service;

import com.dev.br.campeonato_brasileiro.model.Times;
import com.dev.br.campeonato_brasileiro.repository.TimesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimesCampeonatoService {

    @Autowired
    private TimesRepository repository;

    public void inserirEquipes(List<Times> equipes) {
        if (equipes.size() != 20) {
            throw new IllegalArgumentException("A lista deve conter exatamente 20 equipes.");
        }

        repository.saveAll(equipes);
    }

    public List<Times> listarEquipes() {
        return repository.findAll();
    }
}
