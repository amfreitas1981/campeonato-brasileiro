package com.dev.br.campeonato_brasileiro.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;

public class PartidasTest {

    @Test
    @DisplayName("Deve criar uma instância de Partidas com construtor completo")
    public void testCriarPartidaConstrutorCompleto() {
        // Arrange
        Times timesCasa = new Times("Flamengo");
        Times timesFora = new Times("Palmeiras");
        Integer golsCasa = 2;
        Integer golsVisitante = 1;
        int round = 1;
        LocalDate dataPartida = LocalDate.of(2025, 5, 4);

        // Act
        Partidas partida = new Partidas(1L, timesCasa, timesFora, golsCasa, golsVisitante, round, dataPartida);

        // Assert
        assertEquals(1L, partida.getId());
        assertEquals(timesCasa, partida.getTimesCasa());
        assertEquals(timesFora, partida.getTimesFora());
        assertEquals(golsCasa, partida.getGolsCasa());
        assertEquals(golsVisitante, partida.getGolsVisitante());
        assertEquals(round, partida.getRound());
        assertEquals(dataPartida, partida.getDataPartida());
    }

    @Test
    @DisplayName("Deve criar uma instância de Partidas com construtor parcial")
    public void testCriarPartidaConstrutorParcial() {
        // Arrange
        Times timesCasa = new Times("São Paulo");
        Times timesFora = new Times("Corinthians");
        int round = 5;
        LocalDate dataPartida = LocalDate.of(2025, 5, 15);

        // Act
        Partidas partida = new Partidas(timesCasa, timesFora, round, dataPartida);

        // Assert
        assertNull(partida.getId());
        assertEquals(timesCasa, partida.getTimesCasa());
        assertEquals(timesFora, partida.getTimesFora());
        assertNull(partida.getGolsCasa());
        assertNull(partida.getGolsVisitante());
        assertEquals(round, partida.getRound());
        assertEquals(dataPartida, partida.getDataPartida());
    }

    @Test
    @DisplayName("Deve criar uma instância de Partidas com construtor vazio e setters")
    public void testCriarPartidaConstrutorVazioESetters() {
        // Arrange
        Times timesCasa = new Times("Vasco");
        Times timesFora = new Times("Fluminense");
        Integer golsCasa = 3;
        Integer golsVisitante = 3;
        int round = 10;
        LocalDate dataPartida = LocalDate.of(2025, 6, 1);

        // Act
        Partidas partida = new Partidas();
        partida.setId(2L);
        partida.setTimesCasa(timesCasa);
        partida.setTimesFora(timesFora);
        partida.setGolsCasa(golsCasa);
        partida.setGolsVisitante(golsVisitante);
        partida.setRound(round);
        partida.setDataPartida(dataPartida);

        // Assert
        assertEquals(2L, partida.getId());
        assertEquals(timesCasa, partida.getTimesCasa());
        assertEquals(timesFora, partida.getTimesFora());
        assertEquals(golsCasa, partida.getGolsCasa());
        assertEquals(golsVisitante, partida.getGolsVisitante());
        assertEquals(round, partida.getRound());
        assertEquals(dataPartida, partida.getDataPartida());
    }

    @Test
    @DisplayName("Deve testar o método toString")
    public void testToString() {
        // Arrange
        Times timesCasa = new Times(3L, "Grêmio", "GRE", "RS");
        Times timesFora = new Times(4L, "Internacional", "INT", "RS");
        Partidas partida = new Partidas(5L, timesCasa, timesFora, 2, 1, 15, LocalDate.of(2025, 7, 10));

        // Act
        String toStringResult = partida.toString();

        // Assert
        assertTrue(toStringResult.contains("id=5"));
        assertTrue(toStringResult.contains("timesCasa=" + timesCasa.toString()));
        assertTrue(toStringResult.contains("timesFora=" + timesFora.toString()));
        assertTrue(toStringResult.contains("golsCasa=2"));
        assertTrue(toStringResult.contains("golsVisitante=1"));
        assertTrue(toStringResult.contains("round=15"));
        assertTrue(toStringResult.contains("dataPartida=2025-07-10"));
    }
}
