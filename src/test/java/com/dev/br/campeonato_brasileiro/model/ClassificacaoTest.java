package com.dev.br.campeonato_brasileiro.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassificacaoTest {

    private Times timesMock;
    private Classificacao classificacao;

    @BeforeEach
    void setUp() {
        timesMock = mock(Times.class);
        classificacao = new Classificacao(timesMock);
    }

    @Test
    void testInicializacao() {
        assertNotNull(classificacao);
        assertEquals(timesMock, classificacao.getTimes());
    }

    @Test
    void testSetPosicao() {
        classificacao.setPosicao(1);
        assertEquals(1, classificacao.getPosicao());
    }

    @Test
    void testSetPontos() {
        classificacao.setPontos(10);
        assertEquals(10, classificacao.getPontos());
    }

    @Test
    void testSetGolsPro() {
        classificacao.setGolsPro(20);
        assertEquals(20, classificacao.getGolsPro());
    }
}
