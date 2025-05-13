package com.dev.br.campeonato_brasileiro.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.dev.br.campeonato_brasileiro.model.Partidas;
import com.dev.br.campeonato_brasileiro.model.Times;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

class PartidasTest {

    private Times timesCasa;
    private Times timesFora;
    private Partidas partida;

    @BeforeEach
    void setUp() {
        timesCasa = mock(Times.class);
        timesFora = mock(Times.class);
        partida = new Partidas(timesCasa, timesFora, 1, LocalDate.of(2025, 5, 12));
    }

    @Test
    void testCriacaoPartida() {
        assertEquals(timesCasa, partida.getTimesCasa());
        assertEquals(timesFora, partida.getTimesFora());
        assertEquals(1, partida.getRound());
        assertEquals(LocalDate.of(2025, 5, 12), partida.getDataPartida());
    }

    @Test
    void testSetGols() {
        partida.setGolsCasa(2);
        partida.setGolsVisitante(3);

        assertEquals(2, partida.getGolsCasa());
        assertEquals(3, partida.getGolsVisitante());
    }
}
