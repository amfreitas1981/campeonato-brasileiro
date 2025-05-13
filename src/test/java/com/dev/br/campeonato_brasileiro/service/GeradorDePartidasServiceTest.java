package com.dev.br.campeonato_brasileiro.service;

import com.dev.br.campeonato_brasileiro.config.CampeonatoProperties;
import com.dev.br.campeonato_brasileiro.model.Classificacao;
import com.dev.br.campeonato_brasileiro.model.Partidas;
import com.dev.br.campeonato_brasileiro.model.Times;
import com.dev.br.campeonato_brasileiro.repository.ClassificacaoRepository;
import com.dev.br.campeonato_brasileiro.repository.PartidasRepository;
import com.dev.br.campeonato_brasileiro.repository.TimesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeradorDePartidasServiceTest {

    @Mock
    private TimesRepository timesRepository;

    @Mock
    private PartidasRepository partidasRepository;

    @Mock
    private ClassificacaoRepository classificacaoRepository;

    @Mock
    private CampeonatoProperties campeonatoProperties;

    private GeradorDePartidasService geradorDePartidasService;

    @Captor
    private ArgumentCaptor<Classificacao> classificacaoCaptor;

    @Captor
    private ArgumentCaptor<Partidas> partidasCaptor;

    private List<Times> times;
    private LocalDate dataInicio;

    @BeforeEach
    void setUp() {
        // Inicializar o serviço manualmente para injetar os mocks
        geradorDePartidasService = new GeradorDePartidasService(
                timesRepository,
                partidasRepository,
                classificacaoRepository,
                campeonatoProperties
        );

        // Configurar dados de teste
        times = new ArrayList<>();
        times.add(new Times(1L, "Flamengo", "FLA", "RJ"));
        times.add(new Times(2L, "Palmeiras", "PAL", "SP"));
        times.add(new Times(3L, "São Paulo", "SAO", "SP"));
        times.add(new Times(4L, "Grêmio", "GRE", "RS"));

        dataInicio = LocalDate.of(2024, 4, 20);
        when(campeonatoProperties.getDataInicio()).thenReturn(dataInicio);

        lenient().when(campeonatoProperties.getDataInicio()).thenReturn(LocalDate.of(2025, 5, 1));
    }

    @Test
    void gerarPartidas_DeveGerarPartidasDeIdaEVolta() {
        // Act
        List<Partidas> partidas = geradorDePartidasService.gerarPartidas(times);

        // Assert
        int totalTimes = times.size();
        int expectedTotalPartidas = totalTimes * (totalTimes - 1); // Fórmula para jogos de ida e volta

        assertEquals(expectedTotalPartidas, partidas.size());

        // Verifica se as rodadas estão corretas
        int totalRodadas = (totalTimes - 1) * 2;
        for (int rodada = 1; rodada <= totalRodadas; rodada++) {
            final int rodadaAtual = rodada;
            long jogosNaRodada = partidas.stream()
                    .filter(p -> p.getRound() == rodadaAtual)
                    .count();

            assertEquals(totalTimes / 2, jogosNaRodada);
        }

        // Verifica se há partidas de ida e volta entre todos os times
        for (Times timeA : times) {
            for (Times timeB : times) {
                if (!timeA.equals(timeB)) {
                    boolean encontrouTimeAVsTimeB = partidas.stream()
                            .anyMatch(p -> p.getTimesCasa().equals(timeA) && p.getTimesFora().equals(timeB));

                    boolean encontrouTimeBVsTimeA = partidas.stream()
                            .anyMatch(p -> p.getTimesCasa().equals(timeB) && p.getTimesFora().equals(timeA));

                    assertTrue(encontrouTimeAVsTimeB, "Não encontrou partida: " + timeA.getNome() + " vs " + timeB.getNome());
                    assertTrue(encontrouTimeBVsTimeA, "Não encontrou partida: " + timeB.getNome() + " vs " + timeA.getNome());
                }
            }
        }
    }

    @Test
    void testSimularRodada() {
        int round = 1;
        Times timeCasa = new Times("Time A", "TA", "SP");
        Times timeFora = new Times("Time B", "TB", "RJ");
        Partidas partidaMock = new Partidas(timeCasa, timeFora, round, LocalDate.of(2025, 5, 1));

        when(partidasRepository.findByRound(round)).thenReturn(List.of(partidaMock));
        when(classificacaoRepository.findByTimes(any())).thenReturn(Optional.of(new Classificacao(timeCasa)));

        geradorDePartidasService.simularRodada(round);

        verify(partidasRepository, times(1)).save(any(Partidas.class));
        verify(classificacaoRepository, times(2)).save(any(Classificacao.class));
    }

    @Test
    void testSimularRodadaComExcecao() {
        int round = 10;
        when(partidasRepository.findByRound(round)).thenReturn(List.of());

        Exception exception = assertThrows(RuntimeException.class, () -> geradorDePartidasService.simularRodada(round));

        assertEquals("Nenhuma partida encontrada para a rodada " + round, exception.getMessage());
    }

    @Test
    void testAtualizarClassificacao() {
        Times timeCasa = new Times("Time A", "TA", "SP");
        Times timeFora = new Times("Time B", "TB", "RJ");

        Partidas partidaMock = new Partidas(timeCasa, timeFora, 1, LocalDate.of(2025, 5, 1));
        partidaMock.setGolsCasa(2);
        partidaMock.setGolsVisitante(1);

        when(classificacaoRepository.findByTimes(timeCasa)).thenReturn(Optional.of(new Classificacao(timeCasa)));
        when(classificacaoRepository.findByTimes(timeFora)).thenReturn(Optional.of(new Classificacao(timeFora)));

        geradorDePartidasService.atualizarClassificacao(partidaMock);

        verify(classificacaoRepository, times(2)).save(any(Classificacao.class));
    }

    @Test
    void testAtualizarClassificacaoVitoriaVisitante() {
        Times timeCasa = new Times("Time A", "TA", "SP");
        Times timeFora = new Times("Time B", "TB", "RJ");

        Partidas partidaMock = new Partidas(timeCasa, timeFora, 1, LocalDate.of(2025, 5, 1));
        partidaMock.setGolsCasa(1);
        partidaMock.setGolsVisitante(2); // Time visitante vence

        Classificacao classificacaoCasa = new Classificacao(timeCasa);
        Classificacao classificacaoFora = new Classificacao(timeFora);

        when(classificacaoRepository.findByTimes(timeCasa)).thenReturn(Optional.of(classificacaoCasa));
        when(classificacaoRepository.findByTimes(timeFora)).thenReturn(Optional.of(classificacaoFora));

        geradorDePartidasService.atualizarClassificacao(partidaMock);

        assertEquals(1, classificacaoFora.getVitorias());
        assertEquals(3, classificacaoFora.getPontos());
        assertEquals(1, classificacaoCasa.getDerrotas());

        verify(classificacaoRepository, times(2)).save(any(Classificacao.class));
    }

    @Test
    void testAtualizarClassificacaoEmpate() {
        Times timeCasa = new Times("Time A", "TA", "SP");
        Times timeFora = new Times("Time B", "TB", "RJ");

        Partidas partidaMock = new Partidas(timeCasa, timeFora, 1, LocalDate.of(2025, 5, 1));
        partidaMock.setGolsCasa(2);
        partidaMock.setGolsVisitante(2); // Empate

        Classificacao classificacaoCasa = new Classificacao(timeCasa);
        Classificacao classificacaoFora = new Classificacao(timeFora);

        when(classificacaoRepository.findByTimes(timeCasa)).thenReturn(Optional.of(classificacaoCasa));
        when(classificacaoRepository.findByTimes(timeFora)).thenReturn(Optional.of(classificacaoFora));

        geradorDePartidasService.atualizarClassificacao(partidaMock);

        assertEquals(1, classificacaoCasa.getEmpates());
        assertEquals(1, classificacaoFora.getEmpates());
        assertEquals(1, classificacaoCasa.getPontos());
        assertEquals(1, classificacaoFora.getPontos());

        verify(classificacaoRepository, times(2)).save(any(Classificacao.class));
    }
}
