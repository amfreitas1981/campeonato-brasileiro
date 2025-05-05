package com.dev.br.campeonato_brasileiro.service;

import com.dev.br.campeonato_brasileiro.config.CampeonatoProperties;
import com.dev.br.campeonato_brasileiro.model.Classificacao;
import com.dev.br.campeonato_brasileiro.model.Partidas;
import com.dev.br.campeonato_brasileiro.model.Times;
import com.dev.br.campeonato_brasileiro.repository.ClassificacaoRepository;
import com.dev.br.campeonato_brasileiro.repository.PartidasRepository;
import com.dev.br.campeonato_brasileiro.repository.TimesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class GeradorDePartidasService {

    private final TimesRepository timesRepository;
    private final PartidasRepository partidasRepository;
    private final ClassificacaoRepository classificacaoRepository;
    private final CampeonatoProperties campeonatoProperties;

    public GeradorDePartidasService(TimesRepository timesRepository,
                                    PartidasRepository partidasRepository,
                                    ClassificacaoRepository classificacaoRepository,
                                    CampeonatoProperties campeonatoProperties) {
        this.timesRepository = timesRepository;
        this.partidasRepository = partidasRepository;
        this.classificacaoRepository = classificacaoRepository;
        this.campeonatoProperties = campeonatoProperties;
    }

    /**
     * Gera todas as partidas do campeonato, ida e volta.
     */
    public List<Partidas> gerarPartidas(List<Times> times) {
        List<Partidas> partidas = new ArrayList<>();

        int totalTimes = times.size();
        int rounds = (totalTimes - 1) * 2;

        LocalDate dataRodada = campeonatoProperties.getDataInicio();

        for (int round = 0; round < rounds / 2; round++) {
            for (int i = 0; i < totalTimes / 2; i++) {
                Times timesCasa = times.get(i);
                Times timesFora = times.get(totalTimes - 1 - i);
                partidas.add(new Partidas(timesCasa, timesFora, round + 1, dataRodada));
            }
            dataRodada = dataRodada.plusDays(7);
            Collections.rotate(times.subList(1, totalTimes), 1);
        }

        List<Partidas> partidasVolta = new ArrayList<>();
        for (Partidas p : partidas.stream().filter(p -> p.getRound() <= rounds / 2).toList()) {
            int roundVolta = p.getRound() + rounds / 2;
            dataRodada = dataRodada.plusDays(7);
            partidasVolta.add(new Partidas(p.getTimesFora(), p.getTimesCasa(), roundVolta, dataRodada));
        }

        partidas.addAll(partidasVolta);
        return partidas;
    }

    /**
     * Simula o resultado das partidas de uma rodada específica.
     */
    public void simularRodada(int round) {
        List<Partidas> partidas = partidasRepository.findByRound(round);
        if (partidas.isEmpty()) {
            throw new RuntimeException("Nenhuma partida encontrada para a rodada " + round);
        }

        Random random = new Random();

        for (Partidas p : partidas) {
            int golsCasa = random.nextInt(5); // 0 a 4
            int golsVisitante = random.nextInt(5);
            p.setGolsCasa(golsCasa);
            p.setGolsVisitante(golsVisitante);
            partidasRepository.save(p);
            atualizarClassificacao(p);
        }
    }

    /**
     * Atualiza a classificação com base no resultado da partida.
     */
    public void atualizarClassificacao(Partidas partidas) {
        Times timesCasa = partidas.getTimesCasa();
        Times timesFora = partidas.getTimesFora();

        int golsCasa = partidas.getGolsCasa();
        int golsVisitante = partidas.getGolsVisitante();

        Classificacao cCasa = classificacaoRepository.findByTimes(timesCasa)
                .orElse(new Classificacao(timesCasa));
        Classificacao cFora = classificacaoRepository.findByTimes(timesFora)
                .orElse(new Classificacao(timesFora));

        cCasa.setRound(cCasa.getRound() + 1);
        cFora.setRound(cFora.getRound() + 1);

        cCasa.setGolsPro(cCasa.getGolsPro() + golsCasa);
        cCasa.setGolsContra(cCasa.getGolsContra() + golsVisitante);
        cFora.setGolsPro(cFora.getGolsPro() + golsVisitante);
        cFora.setGolsContra(cFora.getGolsContra() + golsCasa);

        if (golsCasa > golsVisitante) {
            cCasa.setVitorias(cCasa.getVitorias() + 1);
            cCasa.setPontos(cCasa.getPontos() + 3);
            cFora.setDerrotas(cFora.getDerrotas() + 1);
        } else if (golsCasa < golsVisitante) {
            cFora.setVitorias(cFora.getVitorias() + 1);
            cFora.setPontos(cFora.getPontos() + 3);
            cCasa.setDerrotas(cCasa.getDerrotas() + 1);
        } else {
            cCasa.setEmpates(cCasa.getEmpates() + 1);
            cFora.setEmpates(cFora.getEmpates() + 1);
            cCasa.setPontos(cCasa.getPontos() + 1);
            cFora.setPontos(cFora.getPontos() + 1);
        }

        // Atualiza saldo de gols
        cCasa.setSaldoGols(cCasa.getGolsPro() - cCasa.getGolsContra());
        cFora.setSaldoGols(cFora.getGolsPro() - cFora.getGolsContra());

        classificacaoRepository.save(cCasa);
        classificacaoRepository.save(cFora);
    }
}
