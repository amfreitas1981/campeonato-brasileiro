package com.dev.br.campeonato_brasileiro.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TimesTest {

    @Test
    @DisplayName("Deve criar um time vazio")
    public void testCriarTimeVazio() {
        Times time = new Times();

        assertNotNull(time);
        assertNull(time.getId());
        assertNull(time.getNome());
        assertNull(time.getSigla());
        assertNull(time.getEstado());
    }

    @Test
    @DisplayName("Deve criar um time apenas com nome")
    public void testCriarTimeComNome() {
        Times time = new Times("São Paulo");

        assertNotNull(time);
        assertNull(time.getId());
        assertEquals("São Paulo", time.getNome());
        assertNull(time.getSigla());
        assertNull(time.getEstado());
    }

    @Test
    @DisplayName("Deve criar um time completo")
    public void testCriarTimeCompleto() {
        Times time = new Times(1L, "Palmeiras", "PAL", "SP");

        assertNotNull(time);
        assertEquals(1L, time.getId());
        assertEquals("Palmeiras", time.getNome());
        assertEquals("PAL", time.getSigla());
        assertEquals("SP", time.getEstado());
    }

    @Test
    @DisplayName("Deve modificar valores de um time")
    public void testModificarTime() {
        Times time = new Times();

        time.setId(2L);
        time.setNome("Corinthians");
        time.setSigla("COR");
        time.setEstado("SP");

        assertEquals(2L, time.getId());
        assertEquals("Corinthians", time.getNome());
        assertEquals("COR", time.getSigla());
        assertEquals("SP", time.getEstado());
    }

    @Test
    @DisplayName("Deve verificar o toString do time")
    public void testToString() {
        Times time = new Times(1L, "Santos", "SAN", "SP");

        String toStringResult = time.toString();

        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("nome=Santos"));
        assertTrue(toStringResult.contains("sigla=SAN"));
        assertTrue(toStringResult.contains("estado=SP"));
    }
}
