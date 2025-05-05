package com.dev.br.campeonato_brasileiro.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClassificacaoDTOTest {

    @Test
    @DisplayName("Deve criar um ClassificacaoDTO corretamente")
    public void testCriarClassificacaoDTO() {
        ClassificacaoDTO dto = new ClassificacaoDTO(
                1, "Flamengo", 30, 15, 9, 3, 3, 25, 10, 15
        );

        assertNotNull(dto);
        assertEquals(1, dto.posicao());
        assertEquals("Flamengo", dto.times());
        assertEquals(30, dto.pontos());
        assertEquals(15, dto.round());
        assertEquals(9, dto.vitorias());
        assertEquals(3, dto.empates());
        assertEquals(3, dto.derrotas());
        assertEquals(25, dto.golsPro());
        assertEquals(10, dto.golsContra());
        assertEquals(15, dto.saldoGols());
    }

    @Test
    @DisplayName("Deve verificar igualdade entre DTOs")
    public void testEqualsClassificacaoDTO() {
        ClassificacaoDTO dto1 = new ClassificacaoDTO(
                1, "Flamengo", 30, 15, 9, 3, 3, 25, 10, 15
        );

        ClassificacaoDTO dto2 = new ClassificacaoDTO(
                1, "Flamengo", 30, 15, 9, 3, 3, 25, 10, 15
        );

        ClassificacaoDTO dto3 = new ClassificacaoDTO(
                2, "São Paulo", 27, 15, 8, 3, 4, 20, 15, 5
        );

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
    }

    @Test
    @DisplayName("Deve verificar hashCode entre DTOs")
    public void testHashCodeClassificacaoDTO() {
        ClassificacaoDTO dto1 = new ClassificacaoDTO(
                1, "Flamengo", 30, 15, 9, 3, 3, 25, 10, 15
        );

        ClassificacaoDTO dto2 = new ClassificacaoDTO(
                1, "Flamengo", 30, 15, 9, 3, 3, 25, 10, 15
        );

        ClassificacaoDTO dto3 = new ClassificacaoDTO(
                2, "São Paulo", 27, 15, 8, 3, 4, 20, 15, 5
        );

        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    @DisplayName("Deve verificar o toString do DTO")
    public void testToStringClassificacaoDTO() {
        ClassificacaoDTO dto = new ClassificacaoDTO(
                1, "Flamengo", 30, 15, 9, 3, 3, 25, 10, 15
        );

        String toStringResult = dto.toString();

        assertTrue(toStringResult.contains("posicao=1"));
        assertTrue(toStringResult.contains("times=Flamengo"));
        assertTrue(toStringResult.contains("pontos=30"));
        assertTrue(toStringResult.contains("round=15"));
        assertTrue(toStringResult.contains("vitorias=9"));
        assertTrue(toStringResult.contains("empates=3"));
        assertTrue(toStringResult.contains("derrotas=3"));
        assertTrue(toStringResult.contains("golsPro=25"));
        assertTrue(toStringResult.contains("golsContra=10"));
        assertTrue(toStringResult.contains("saldoGols=15"));
    }
}
