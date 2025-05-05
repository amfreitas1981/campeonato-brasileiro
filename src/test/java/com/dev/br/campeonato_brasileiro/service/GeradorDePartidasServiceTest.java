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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

//    @Test
//    void simularRodada_DeveSimularTodasAsPartidasDaRodada() {
//        // Arrange
//        int rodada = 1;
//        List<Partidas> partidasRodada = new ArrayList<>();
//        partidasRodada.add(new Partidas(times.get(0), times.get(1), rodada, dataInicio));
//        partidasRodada.add(new Partidas(times.get(2), times.get(3), rodada, dataInicio));
//
//        when(partidasRepository.findByRound(rodada)).thenReturn(partidasRodada);
//        when(partidasRepository.save(any(Partidas.class))).thenAnswer(i -> i.getArgument(0));
//        when(classificacaoRepository.findByTimes(any(Times.class))).thenReturn(Optional.empty());
//
//        // Act
//        geradorDePartidasService.simularRodada(rodada);
//
//        // Assert
//        verify(partidasRepository, times(2)).save(partidasCaptor.capture());
//        List<Partidas> partidasCapturadas = partidasCaptor.getAllValues();
//
//        for (Partidas partida : partidasCapturadas) {
//            assertNotNull(partida.getGolsCasa());
//            assertNotNull(partida.getGolsVisitante());
//            assertTrue(partida.getGolsCasa() >= 0 && partida.getGolsCasa() <= 4);
//            assertTrue(partida.getGolsVisitante() >= 0 && partida.getGolsVisitante() <= 4);
//        }
//
//        // Verificar que a classificação foi atualizada
//        verify(classificacaoRepository, times(4)).save(any(Classificacao.class));
//    }
//
//    @Test
//    void simularRodada_QuandoRodadaNaoExiste_DeveLancarException() {
//        // Arrange
//        int rodada = 10;
//        when(partidasRepository.findByRound(rodada)).thenReturn(new ArrayList<>());
//
//        // Act & Assert
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            geradorDePartidasService.simularRodada(rodada);
//        });
//
//        assertEquals("Nenhuma partida encontrada para a rodada " + rodada, exception.getMessage());
//    }
//
//    @Test
//    void atualizarClassificacao_QuandoTimeCasaVence_DeveAtualizarPontuacaoCorretamente() {
//        // Arrange
//        Times timeCasa = times.get(0);
//        Times timeFora = times.get(1);
//
//        Partidas partida = new Partidas();
//        partida.setTimesCasa(timeCasa);
//        partida.setTimesFora(timeFora);
//        partida.setGolsCasa(3);
//        partida.setGolsVisitante(1);
//
//        Classificacao classificacaoCasa = new Classificacao(timeCasa);
//        Classificacao classificacaoFora = new Classificacao(timeFora);
//
//        when(classificacaoRepository.findByTimes(timeCasa)).thenReturn(Optional.of(classificacaoCasa));
//        when(classificacaoRepository.findByTimes(timeFora)).thenReturn(Optional.of(classificacaoFora));
//
//        // Act
//        geradorDePartidasService.atualizarClassificacao(partida);
//
//        // Assert
//        verify(classificacaoRepository, times(2)).save(classificacaoCaptor.capture());
//        List<Classificacao> classificacoesCapturadas = classificacaoCaptor.getAllValues();
//
//        Classificacao classificacaoCasaAtualizada = classificacoesCapturadas.get(0);
//        Classificacao classificacaoForaAtualizada = classificacoesCapturadas.get(1);
//
//        // Verificando time casa (vencedor)
//        assertEquals(1, classificacaoCasaAtualizada.getRound());
//        assertEquals(3, classificacaoCasaAtualizada.getPontos());
//        assertEquals(1, classificacaoCasaAtualizada.getVitorias());
//        assertEquals(0, classificacaoCasaAtualizada.getEmpates());
//        assertEquals(0, classificacaoCasaAtualizada.getDerrotas());
//        assertEquals(3, classificacaoCasaAtualizada.getGolsPro());
//        assertEquals(1, classificacaoCasaAtualizada.getGolsContra());
//        assertEquals(2, classificacaoCasaAtualizada.getSaldoGols());
//
//        // Verificando time fora (perdedor)
//        assertEquals(1, classificacaoForaAtualizada.getRound());
//        assertEquals(0, classificacaoForaAtualizada.getPontos());
//        assertEquals(0, classificacaoForaAtualizada.getVitorias());
//        assertEquals(0, classificacaoForaAtualizada.getEmpates());
//        assertEquals(1, classificacaoForaAtualizada.getDerrotas());
//        assertEquals(1, classificacaoForaAtualizada.getGolsPro());
//        assertEquals(3, classificacaoForaAtualizada.getGolsContra());
//        assertEquals(-2, classificacaoForaAtualizada.getSaldoGols());
//    }
//
//    @Test
//    void atualizarClassificacao_QuandoTimeForaVence_DeveAtualizarPontuacaoCorretamente() {
//        // Arrange
//        Times timeCasa = times.get(0);
//        Times timeFora = times.get(1);
//
//        Partidas partida = new Partidas();
//        partida.setTimesCasa(timeCasa);
//        partida.setTimesFora(timeFora);
//        partida.setGolsCasa(0);
//        partida.setGolsVisitante(2);
//
//        Classificacao classificacaoCasa = new Classificacao(timeCasa);
//        Classificacao classificacaoFora = new Classificacao(timeFora);
//
//        when(classificacaoRepository.findByTimes(timeCasa)).thenReturn(Optional.of(classificacaoCasa));
//        when(classificacaoRepository.findByTimes(timeFora)).thenReturn(Optional.of(classificacaoFora));
//
//        // Act
//        geradorDePartidasService.atualizarClassificacao(partida);
//
//        // Assert
//        verify(classificacaoRepository, times(2)).save(classificacaoCaptor.capture());
//        List<Classificacao> classificacoesCapturadas = classificacaoCaptor.getAllValues();
//
//        Classificacao classificacaoCasaAtualizada = classificacoesCapturadas.get(0);
//        Classificacao classificacaoForaAtualizada = classificacoesCapturadas.get(1);
//
//        // Verificando time casa (perdedor)
//        assertEquals(1, classificacaoCasaAtualizada.getRound());
//        assertEquals(0, classificacaoCasaAtualizada.getPontos());
//        assertEquals(0, classificacaoCasaAtualizada.getVitorias());
//        assertEquals(0, classificacaoCasaAtualizada.getEmpates());
//        assertEquals(1, classificacaoCasaAtualizada.getDerrotas());
//        assertEquals(0, classificacaoCasaAtualizada.getGolsPro());
//        assertEquals(2, classificacaoCasaAtualizada.getGolsContra());
//        assertEquals(-2, classificacaoCasaAtualizada.getSaldoGols());
//
//        // Verificando time fora (vencedor)
//        assertEquals(1, classificacaoForaAtualizada.getRound());
//        assertEquals(3, classificacaoForaAtualizada.getPontos());
//        assertEquals(1, classificacaoForaAtualizada.getVitorias());
//        assertEquals(0, classificacaoForaAtualizada.getEmpates());
//        assertEquals(0, classificacaoForaAtualizada.getDerrotas());
//        assertEquals(2, classificacaoForaAtualizada.getGolsPro());
//        assertEquals(0, classificacaoForaAtualizada.getGolsContra());
//        assertEquals(2, classificacaoForaAtualizada.getSaldoGols());
//    }
//
//    @Test
//    void atualizarClassificacao_QuandoEmpate_DeveAtualizarPontuacaoCorretamente() {
//        // Arrange
//        Times timeCasa = times.get(0);
//        Times timeFora = times.get(1);
//
//        Partidas partida = new Partidas();
//        partida.setTimesCasa(timeCasa);
//        partida.setTimesFora(timeFora);
//        partida.setGolsCasa(1);
//        partida.setGolsVisitante(1);
//
//        Classificacao classificacaoCasa = new Classificacao(timeCasa);
//        Classificacao classificacaoFora = new Classificacao(timeFora);
//
//        when(classificacaoRepository.findByTimes(timeCasa)).thenReturn(Optional.of(classificacaoCasa));
//        when(classificacaoRepository.findByTimes(timeFora)).thenReturn(Optional.of(classificacaoFora));
//
//        // Act
//        geradorDePartidasService.atualizarClassificacao(partida);
//
//        // Assert
//        verify(classificacaoRepository, times(2)).save(classificacaoCaptor.capture());
//        List<Classificacao> classificacoesCapturadas = classificacaoCaptor.getAllValues();
//
//        Classificacao classificacaoCasaAtualizada = classificacoesCapturadas.get(0);
//        Classificacao classificacaoForaAtualizada = classificacoesCapturadas.get(1);
//
//        // Verificando time casa (empate)
//        assertEquals(1, classificacaoCasaAtualizada.getRound());
//        assertEquals(1, classificacaoCasaAtualizada.getPontos());
//        assertEquals(0, classificacaoCasaAtualizada.getVitorias());
//        assertEquals(1, classificacaoCasaAtualizada.getEmpates());
//        assertEquals(0, classificacaoCasaAtualizada.getDerrotas());
//        assertEquals(1, classificacaoCasaAtualizada.getGolsPro());
//        assertEquals(1, classificacaoCasaAtualizada.getGolsContra());
//        assertEquals(0, classificacaoCasaAtualizada.getSaldoGols());
//
//        // Verificando time fora (empate)
//        assertEquals(1, classificacaoForaAtualizada.getRound());
//        assertEquals(1, classificacaoForaAtualizada.getPontos());
//        assertEquals(0, classificacaoForaAtualizada.getVitorias());
//        assertEquals(1, classificacaoForaAtualizada.getEmpates());
//        assertEquals(0, classificacaoForaAtualizada.getDerrotas());
//        assertEquals(1, classificacaoForaAtualizada.getGolsPro());
//        assertEquals(1, classificacaoForaAtualizada.getGolsContra());
//        assertEquals(0, classificacaoForaAtualizada.getSaldoGols());
//    }
//
//    @Test
//    void atualizarClassificacao_QuandoClassificacaoNaoExiste_DeveCriarUmaNova() {
//        // Arrange
//        Times timeCasa = times.get(0);
//        Times timeFora = times.get(1);
//
//        Partidas partida = new Partidas();
//        partida.setTimesCasa(timeCasa);
//        partida.setTimesFora(timeFora);
//        partida.setGolsCasa(2);
//        partida.setGolsVisitante(0);
//
//        when(classificacaoRepository.findByTimes(timeCasa)).thenReturn(Optional.empty());
//        when(classificacaoRepository.findByTimes(timeFora)).thenReturn(Optional.empty());
//
//        // Act
//        geradorDePartidasService.atualizarClassificacao(partida);
//
//        // Assert
//        verify(classificacaoRepository, times(2)).save(classificacaoCaptor.capture());
//        List<Classificacao> classificacoesCapturadas = classificacaoCaptor.getAllValues();
//
//        Classificacao classificacaoCasaAtualizada = classificacoesCapturadas.get(0);
//        Classificacao classificacaoForaAtualizada = classificacoesCapturadas.get(1);
//
//        // Verificando time casa (vencedor)
//        assertEquals(timeCasa, classificacaoCasaAtualizada.getTimes());
//        assertEquals(1, classificacaoCasaAtualizada.getRound());
//        assertEquals(3, classificacaoCasaAtualizada.getPontos());
//        assertEquals(1, classificacaoCasaAtualizada.getVitorias());
//        assertEquals(0, classificacaoCasaAtualizada.getEmpates());
//        assertEquals(0, classificacaoCasaAtualizada.getDerrotas());
//        assertEquals(2, classificacaoCasaAtualizada.getGolsPro());
//        assertEquals(0, classificacaoCasaAtualizada.getGolsContra());
//        assertEquals(2, classificacaoCasaAtualizada.getSaldoGols());
//
//        // Verificando time fora (perdedor)
//        assertEquals(timeFora, classificacaoForaAtualizada.getTimes());
//        assertEquals(1, classificacaoForaAtualizada.getRound());
//        assertEquals(0, classificacaoForaAtualizada.getPontos());
//        assertEquals(0, classificacaoForaAtualizada.getVitorias());
//        assertEquals(0, classificacaoForaAtualizada.getEmpates());
//        assertEquals(1, classificacaoForaAtualizada.getDerrotas());
//        assertEquals(0, classificacaoForaAtualizada.getGolsPro());
//        assertEquals(2, classificacaoForaAtualizada.getGolsContra());
//        assertEquals(-2, classificacaoForaAtualizada.getSaldoGols());
//    }
}
