package com.dev.br.campeonato_brasileiro.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClassificacaoTest {

    private Times time;
    private Classificacao classificacao;

    @BeforeEach
    public void setup() {
        time = new Times("Flamengo");
        time.setId(1L);
        time.setSigla("FLA");
        time.setEstado("RJ");

        classificacao = new Classificacao();
    }

    @Test
    @DisplayName("Deve criar uma classificação vazia")
    public void testCriarClassificacaoVazia() {
        assertNotNull(classificacao);
        assertNull(classificacao.getId());
        assertNull(classificacao.getTimes());
        assertEquals(0, classificacao.getPosicao());
        assertEquals(0, classificacao.getPontos());
        assertEquals(0, classificacao.getRound());
        assertEquals(0, classificacao.getVitorias());
        assertEquals(0, classificacao.getEmpates());
        assertEquals(0, classificacao.getDerrotas());
        assertEquals(0, classificacao.getGolsPro());
        assertEquals(0, classificacao.getGolsContra());
        assertEquals(0, classificacao.getSaldoGols());
    }

    @Test
    @DisplayName("Deve criar uma classificação com time")
    public void testCriarClassificacaoComTime() {
        Classificacao classificacaoComTime = new Classificacao(time);

        assertNotNull(classificacaoComTime);
        assertNull(classificacaoComTime.getId());
        assertEquals(time, classificacaoComTime.getTimes());
        assertEquals(0, classificacaoComTime.getPosicao());
        assertEquals(0, classificacaoComTime.getPontos());
    }

    @Test
    @DisplayName("Deve criar uma classificação com todos os atributos")
    public void testCriarClassificacaoCompleta() {
        Classificacao classificacaoCompleta = new Classificacao(
                1L, 1, time, 10, 5, 3, 1, 1, 7, 3, 4
        );

        assertNotNull(classificacaoCompleta);
        assertEquals(1L, classificacaoCompleta.getId());
        assertEquals(1, classificacaoCompleta.getPosicao());
        assertEquals(time, classificacaoCompleta.getTimes());
        assertEquals(10, classificacaoCompleta.getPontos());
        assertEquals(5, classificacaoCompleta.getRound());
        assertEquals(3, classificacaoCompleta.getVitorias());
        assertEquals(1, classificacaoCompleta.getEmpates());
        assertEquals(1, classificacaoCompleta.getDerrotas());
        assertEquals(7, classificacaoCompleta.getGolsPro());
        assertEquals(3, classificacaoCompleta.getGolsContra());
        assertEquals(4, classificacaoCompleta.getSaldoGols());
    }

    @Test
    @DisplayName("Deve modificar valores de uma classificação")
    public void testModificarClassificacao() {
        classificacao.setId(2L);
        classificacao.setPosicao(3);
        classificacao.setTimes(time);
        classificacao.setPontos(12);
        classificacao.setRound(7);
        classificacao.setVitorias(4);
        classificacao.setEmpates(0);
        classificacao.setDerrotas(3);
        classificacao.setGolsPro(10);
        classificacao.setGolsContra(8);
        classificacao.setSaldoGols(2);

        assertEquals(2L, classificacao.getId());
        assertEquals(3, classificacao.getPosicao());
        assertEquals(time, classificacao.getTimes());
        assertEquals(12, classificacao.getPontos());
        assertEquals(7, classificacao.getRound());
        assertEquals(4, classificacao.getVitorias());
        assertEquals(0, classificacao.getEmpates());
        assertEquals(3, classificacao.getDerrotas());
        assertEquals(10, classificacao.getGolsPro());
        assertEquals(8, classificacao.getGolsContra());
        assertEquals(2, classificacao.getSaldoGols());
    }

    @Test
    @DisplayName("Deve verificar o toString da classificação")
    public void testToString() {
        Classificacao classificacaoCompleta = new Classificacao(
                1L, 1, time, 10, 5, 3, 1, 1, 7, 3, 4
        );

        String toStringResult = classificacaoCompleta.toString();

        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("posicao=1"));
        assertTrue(toStringResult.contains("times=" + time.toString()));
        assertTrue(toStringResult.contains("pontos=10"));
        assertTrue(toStringResult.contains("round=5"));
        assertTrue(toStringResult.contains("vitorias=3"));
        assertTrue(toStringResult.contains("empates=1"));
        assertTrue(toStringResult.contains("derrotas=1"));
        assertTrue(toStringResult.contains("golsPro=7"));
        assertTrue(toStringResult.contains("golsContra=3"));
        assertTrue(toStringResult.contains("saldoGols=4"));
    }
}
