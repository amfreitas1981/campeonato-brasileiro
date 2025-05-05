package com.dev.br.campeonato_brasileiro.controller;

import com.dev.br.campeonato_brasileiro.dto.ClassificacaoDTO;
import com.dev.br.campeonato_brasileiro.service.ClassificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/classification")
@RequiredArgsConstructor
public class ClassificacaoController {

    private final ClassificacaoService classificacaoService;

    @GetMapping("/general")
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

    @GetMapping("/downgrade")
    public List<ClassificacaoDTO> getRebaixamento() {
        return classificacaoService.getRebaixamento();
    }
}
