package com.dev.br.campeonato_brasileiro.service;

import com.dev.br.campeonato_brasileiro.dto.ClassificacaoDTO;
import com.dev.br.campeonato_brasileiro.model.Classificacao;
import com.dev.br.campeonato_brasileiro.model.Times;
import com.dev.br.campeonato_brasileiro.repository.ClassificacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ClassificacaoServiceTest {

    @Mock
    private ClassificacaoRepository classificacaoRepository;

    @InjectMocks
    private ClassificacaoService classificacaoService;

    private List<Classificacao> mockClassificacoes;

    @BeforeEach
    void setUp() {
        mockClassificacoes = Arrays.asList(
                criarClassificacao("Time A", 30, 9, 3, 3, 25, 10),
                criarClassificacao("Time B", 28, 8, 4, 3, 20, 12),
                criarClassificacao("Time C", 25, 7, 4, 4, 18, 13),
                criarClassificacao("Time D", 20, 6, 2, 7, 15, 20)
        );
    }

    private Classificacao criarClassificacao(String nomeTime, int pontos, int vitorias, int empates, int derrotas, int golsPro, int golsContra) {
        Times time = new Times();
        time.setNome(nomeTime);

        Classificacao c = new Classificacao();
        c.setTimes(time);
        c.setPontos(pontos);
        c.setRound(15);
        c.setVitorias(vitorias);
        c.setEmpates(empates);
        c.setDerrotas(derrotas);
        c.setGolsPro(golsPro);
        c.setGolsContra(golsContra);
        c.setSaldoGols(golsPro - golsContra);
        return c;
    }

    @Test
    void listarClassificacaoGeral_deveRetornarDTOsOrdenados() {
        when(classificacaoRepository.findAllByOrderByPontosDescVitoriasDescSaldoGolsDescGolsProDesc()).thenReturn(mockClassificacoes);

        List<ClassificacaoDTO> resultado = classificacaoService.listarClassificacaoGeral();

        assertEquals(4, resultado.size());
        assertEquals("Time A", resultado.get(0).times());
        assertEquals(1, resultado.get(0).posicao());
    }

    @Test
    void getClassificacaoOrdenada_deveRetornarListaOrdenadaPorCriterios() {
        when(classificacaoRepository.findAll()).thenReturn(mockClassificacoes);

        List<Classificacao> resultado = classificacaoService.getClassificacaoOrdenada();

        assertEquals(4, resultado.size());
        assertEquals("Time A", resultado.get(0).getTimes().getNome());
        assertTrue(resultado.get(0).getPontos() >= resultado.get(1).getPontos());
    }

    @Test
    void getClassificacaoOrdenadaDTO_deveConverterParaDTOsComPosicoes() {
        when(classificacaoRepository.findAll()).thenReturn(mockClassificacoes);

        List<ClassificacaoDTO> dtos = classificacaoService.getClassificacaoOrdenadaDTO();

        assertEquals(4, dtos.size());
        assertEquals(1, dtos.get(0).posicao());
        assertEquals("Time A", dtos.get(0).times());
    }

    @Test
    void getClassificacaoDTOByInterval_deveRetornarSubListaValida() {
        when(classificacaoRepository.findAll()).thenReturn(mockClassificacoes);

        List<ClassificacaoDTO> sublista = classificacaoService.getClassificacaoDTOByInterval(0, 2);

        assertEquals(2, sublista.size());
        assertEquals("Time A", sublista.get(0).times());
    }

    @Test
    void getClassificacaoDTOByInterval_comIndiceInvalido_deveRetornarListaVazia() {
        when(classificacaoRepository.findAll()).thenReturn(mockClassificacoes);

        List<ClassificacaoDTO> sublista = classificacaoService.getClassificacaoDTOByInterval(-1, 10);

        assertTrue(sublista.isEmpty());
    }

//    @Test
//    void getLibertadores_deveRetornar6Primeiros() {
//        when(classificacaoRepository.findAllByOrderByPontosDescVitoriasDescSaldoGolsDescGolsProDesc()).thenReturn(mockClassificacoes);
//
//        List<ClassificacaoDTO> libertadores = classificacaoService.getLibertadores();
//
//        assertEquals(4, libertadores.size()); // pois só mockamos 4
//    }
//
//    @Test
//    void getSulAmericana_deveRetornarIntervaloCorreto() {
//        when(classificacaoRepository.findAllByOrderByPontosDescVitoriasDescSaldoGolsDescGolsProDesc()).thenReturn(mockClassificacoes);
//
//        List<ClassificacaoDTO> sulAmericana = classificacaoService.getSulAmericana();
//
//        assertTrue(sulAmericana.isEmpty()); // pois só temos 4 mockados
//    }
//
//    @Test
//    void getRebaixamento_deveRetornar4Ultimos() {
//        when(classificacaoRepository.findAllByOrderByPontosDescVitoriasDescSaldoGolsDescGolsProDesc()).thenReturn(mockClassificacoes);
//
//        List<ClassificacaoDTO> rebaixamento = classificacaoService.getRebaixamento();
//
//        assertTrue(rebaixamento.isEmpty()); // pois só temos 4 mockados
//    }
}
