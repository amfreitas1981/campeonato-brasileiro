package com.dev.br.campeonato_brasileiro.controller;

import com.dev.br.campeonato_brasileiro.model.Times;
import com.dev.br.campeonato_brasileiro.service.TimesCampeonatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TimesCampeonatoController {

    @Autowired
    private TimesCampeonatoService service;

    @PostMapping("/insert")
    public ResponseEntity<String> inserirEquipes(@RequestBody List<Times> equipes) {
        service.inserirEquipes(equipes);
        return ResponseEntity.ok("Equipes inseridas com sucesso.");
    }

    @GetMapping("/consult")
    public ResponseEntity<List<Times>> listarEquipes() {
        return ResponseEntity.ok(service.listarEquipes());
    }
}
