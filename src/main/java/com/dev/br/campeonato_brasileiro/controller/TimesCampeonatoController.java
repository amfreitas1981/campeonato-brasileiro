package com.dev.br.campeonato_brasileiro.controller;

import com.dev.br.campeonato_brasileiro.dto.TimesDTO;
import com.dev.br.campeonato_brasileiro.model.Times;
import com.dev.br.campeonato_brasileiro.service.TimesCampeonatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> inserirEquipes(@RequestBody List<TimesDTO> equipes) {
        try {
            service.inserirEquipes(equipes);
            return ResponseEntity.ok("Equipes inseridas com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir equipes: " + e.getMessage());
        }
    }

    @GetMapping("/consult")
    public ResponseEntity<List<Times>> listarEquipes() {
        return ResponseEntity.ok(service.listarEquipes());
    }
}
