package com.dev.br.campeonato_brasileiro.controller;

import com.dev.br.campeonato_brasileiro.dto.ClassificacaoDTO;
import com.dev.br.campeonato_brasileiro.service.ClassificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classificacao")
@RequiredArgsConstructor
public class ClassificacaoController {

    private final ClassificacaoService classificacaoService;

    @GetMapping("/geral")
    public List<ClassificacaoDTO> getClassificacaoGeral() {
        return classificacaoService.listarClassificacaoGeral();
    }

    @GetMapping("/libertadores")
    public List<ClassificacaoDTO> getLibertadores() {
        return classificacaoService.getLibertadores();
    }

    @GetMapping("/sulamericana")
    public List<ClassificacaoDTO> getSulAmericana() {
        return classificacaoService.getSulAmericana();
    }

    @GetMapping("/rebaixamento")
    public List<ClassificacaoDTO> getRebaixamento() {
        return classificacaoService.getRebaixamento();
    }
}