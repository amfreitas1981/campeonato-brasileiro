package com.dev.br.campeonato_brasileiro.service;

import com.dev.br.campeonato_brasileiro.dto.ClassificacaoDTO;
import com.dev.br.campeonato_brasileiro.model.Classificacao;
import com.dev.br.campeonato_brasileiro.repository.ClassificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassificacaoService {

    private final ClassificacaoRepository classificacaoRepository;

    public List<ClassificacaoDTO> listarClassificacaoGeral() {
        List<Classificacao> lista = classificacaoRepository.findAllByOrderByPontosDescVitoriasDescSaldoGolsDescGolsProDesc();
        List<ClassificacaoDTO> dtosGeral = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            Classificacao c = lista.get(i);
            dtosGeral.add(new ClassificacaoDTO(
                    i + 1,
                    c.getTimes().getNome(),
                    c.getPontos(),
                    c.getRound(),
                    c.getVitorias(),
                    c.getEmpates(),
                    c.getDerrotas(),
                    c.getGolsPro(),
                    c.getGolsContra(),
                    c.getSaldoGols()
            ));
        }
        return dtosGeral;
    }

    /**
     * Retorna a classificação por intervalos na tabela
     * 1. Título/Libertadores
     * 2. Sul Americana
     * 3. Rebaixamento
     */
    public List<ClassificacaoDTO> getClassificacaoDTOByInterval(int inicio, int fim) {
        List<ClassificacaoDTO> lista = getClassificacaoOrdenadaDTO();
        if (inicio >= 0 && fim <= lista.size()) {
            return lista.subList(inicio, fim);
        }
        return new ArrayList<>();
    }

    /**
     * Retorna a classificação ordenada por:
     * 1. Pontos
     * 2. Saldo de Gols
     * 3. Gols Pró
     */
    public List<Classificacao> getClassificacaoOrdenada() {
        List<Classificacao> classificacoes = classificacaoRepository.findAll();

        classificacoes.sort(Comparator
                .comparing(Classificacao::getPontos).reversed()
                .thenComparing(Classificacao::getSaldoGols, Comparator.reverseOrder())
                .thenComparing(Classificacao::getGolsPro, Comparator.reverseOrder()));

        return classificacoes;
    }

    public List<ClassificacaoDTO> getClassificacaoOrdenadaDTO() {
        List<Classificacao> lista = getClassificacaoOrdenada();

        List<ClassificacaoDTO> dtos = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            Classificacao c = lista.get(i);
            dtos.add(new ClassificacaoDTO(
                    i + 1,
                    c.getTimes().getNome(),
                    c.getPontos(),
                    c.getRound(),
                    c.getVitorias(),
                    c.getEmpates(),
                    c.getDerrotas(),
                    c.getGolsPro(),
                    c.getGolsContra(),
                    c.getSaldoGols()
            ));
        }
        return dtos;
    }

    public List<ClassificacaoDTO> getLibertadores() {
        return listarClassificacaoGeral().subList(0, 6);
    }

    public List<ClassificacaoDTO> getSulAmericana() {
        return listarClassificacaoGeral().subList(6, 12);
    }

    public List<ClassificacaoDTO> getRebaixamento() {
        return listarClassificacaoGeral().subList(16, 20);
    }
}