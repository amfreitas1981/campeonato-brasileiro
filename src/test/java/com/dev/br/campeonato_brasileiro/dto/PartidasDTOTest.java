package com.dev.br.campeonato_brasileiro.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;

public class PartidasDTOTest {

    @Test
    @DisplayName("Deve criar um PartidasDTO com todos os campos")
    public void testCriarPartidasDTO() {
        // Arrange
        String timesCasa = "Flamengo";
        String timesFora = "Palmeiras";
        Integer golsCasa = 2;
        Integer golsVisitante = 1;
        int rodada = 3;
        LocalDate dataPartida = LocalDate.of(2025, 5, 10);

        // Act
        PartidasDTO partidasDTO = new PartidasDTO(timesCasa, timesFora, golsCasa, golsVisitante, rodada, dataPartida);

        // Assert
        assertEquals(timesCasa, partidasDTO.timesCasa());
        assertEquals(timesFora, partidasDTO.timesFora());
        assertEquals(golsCasa, partidasDTO.golsCasa());
        assertEquals(golsVisitante, partidasDTO.golsVisitante());
        assertEquals(rodada, partidasDTO.rodada());
        assertEquals(dataPartida, partidasDTO.dataPartida());
    }

    @Test
    @DisplayName("Deve testar a igualdade de dois PartidasDTO idênticos")
    public void testIgualdadePartidasDTO() {
        // Arrange
        LocalDate dataPartida = LocalDate.of(2025, 5, 15);
        PartidasDTO partidasDTO1 = new PartidasDTO("São Paulo", "Corinthians", 1, 1, 5, dataPartida);
        PartidasDTO partidasDTO2 = new PartidasDTO("São Paulo", "Corinthians", 1, 1, 5, dataPartida);

        // Assert
        assertEquals(partidasDTO1, partidasDTO2);
        assertEquals(partidasDTO1.hashCode(), partidasDTO2.hashCode());
    }

    @Test
    @DisplayName("Deve testar a diferença de dois PartidasDTO distintos")
    public void testDiferencaPartidasDTO() {
        // Arrange
        LocalDate dataPartida = LocalDate.of(2025, 5, 20);
        PartidasDTO partidasDTO1 = new PartidasDTO("Vasco", "Fluminense", 0, 2, 7, dataPartida);
        PartidasDTO partidasDTO2 = new PartidasDTO("Vasco", "Fluminense", 1, 2, 7, dataPartida);

        // Assert
        assertNotEquals(partidasDTO1, partidasDTO2);
    }

    @Test
    @DisplayName("Deve testar o método toString")
    public void testToString() {
        // Arrange
        LocalDate dataPartida = LocalDate.of(2025, 6, 5);
        PartidasDTO partidasDTO = new PartidasDTO("Athletico-PR", "Coritiba", 3, 0, 12, dataPartida);

        // Act
        String toString = partidasDTO.toString();

        // Assert
        assertTrue(toString.contains("Athletico-PR"));
        assertTrue(toString.contains("Coritiba"));
        assertTrue(toString.contains("3"));
        assertTrue(toString.contains("0"));
        assertTrue(toString.contains("12"));
        assertTrue(toString.contains("2025-06-05"));
    }
}
