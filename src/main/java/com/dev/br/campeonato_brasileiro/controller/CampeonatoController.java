package com.dev.br.campeonato_brasileiro.controller;

import com.dev.br.campeonato_brasileiro.dto.ClassificacaoDTO;
import com.dev.br.campeonato_brasileiro.model.Partidas;
import com.dev.br.campeonato_brasileiro.model.Times;
import com.dev.br.campeonato_brasileiro.repository.ClassificacaoRepository;
import com.dev.br.campeonato_brasileiro.repository.PartidasRepository;
import com.dev.br.campeonato_brasileiro.repository.TimesRepository;
import com.dev.br.campeonato_brasileiro.service.ClassificacaoService;
import com.dev.br.campeonato_brasileiro.service.ExportacaoService;
import com.dev.br.campeonato_brasileiro.service.GeradorDePartidasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/championship")
@RequiredArgsConstructor
public class CampeonatoController {

    private final TimesRepository timesRepository;
    private final PartidasRepository partidasRepository;
    private final ClassificacaoRepository classificacaoRepository;
    private final GeradorDePartidasService geradorDePartidasService;
    private final ClassificacaoService classificacaoService;
    private final ExportacaoService exportacaoService;

    @GetMapping("/round/{number}/matches")
    public ResponseEntity<List<Partidas>> listarPartidasPorRodada(@PathVariable int number) {
        return ResponseEntity.ok(partidasRepository.findByRound(number));
    }

    @DeleteMapping("/restart")
    public ResponseEntity<String> resetarCampeonato() {
        classificacaoRepository.deleteAll();
        partidasRepository.deleteAll();
        return ResponseEntity.ok("Campeonato reiniciado com sucesso.");
    }

    @GetMapping("/export-classification")
    public ResponseEntity<byte[]> exportarClassificacaoParaExcel() {
        ByteArrayInputStream stream = exportacaoService.exportarClassificacaoParaExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=classificacao.xlsx");
        return ResponseEntity.ok().headers(headers).body(stream.readAllBytes());
    }

    @GetMapping("/classification")
    public ResponseEntity<List<ClassificacaoDTO>> getClassificacao() {
        return ResponseEntity.ok(classificacaoService.getClassificacaoOrdenadaDTO());
    }

    @GetMapping("/classification/libertadores")
    public ResponseEntity<List<ClassificacaoDTO>> getLibertadores() {
        return ResponseEntity.ok(classificacaoService.getClassificacaoDTOByInterval(0, 6));
    }

    @GetMapping("/classification/sulamericana")
    public ResponseEntity<List<ClassificacaoDTO>> getSulAmericana() {
        return ResponseEntity.ok(classificacaoService.getClassificacaoDTOByInterval(6, 12));
    }

    @GetMapping("/classification/downgrade")
    public ResponseEntity<List<ClassificacaoDTO>> getRebaixamento() {
        return ResponseEntity.ok(classificacaoService.getClassificacaoDTOByInterval(16, 20));
    }

    // TODO: Gerar e salvar as partidas
    @PostMapping("/generate-rounds")
    public ResponseEntity<String> gerarPartidas() {
        List<Times> times = timesRepository.findAll();
        List<Partidas> partidas = geradorDePartidasService.gerarPartidas(times);
        partidasRepository.saveAll(partidas);
        return ResponseEntity.ok("Partidas geradas e salvas com sucesso.");
    }

    // TODO: Consultar para saber se as partidas foram geradas
    @GetMapping("/rounds")
    public ResponseEntity<List<Partidas>> listarPartidas() {
        return ResponseEntity.ok(partidasRepository.findAll());
    }

    // TODO: Simular as partidas e verificar se as partidas existem antes da simulação
    @PostMapping("/simulation")
    public ResponseEntity<String> simularCampeonato() {
        List<Partidas> partidas = partidasRepository.findAll();
        if (partidas.isEmpty()) {
            List<Times> times = timesRepository.findAll();
            List<Partidas> novasPartidas = geradorDePartidasService.gerarPartidas(times);
            partidasRepository.saveAll(novasPartidas);
        }

        for (int round = 1; round <= 38; round++) {
            geradorDePartidasService.simularRodada(round);
        }

        return ResponseEntity.ok("Simulação concluída!");
    }
}
