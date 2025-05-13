package com.dev.br.campeonato_brasileiro.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TimesTest {

    @Test
    void testConstructorWithParameters() {
        Times times = new Times("Flamengo", "FLA", "RJ");

        assertEquals("Flamengo", times.getNome());
        assertEquals("FLA", times.getSigla());
        assertEquals("RJ", times.getEstado());
    }

    @Test
    void testSetterMethods() {
        Times times = new Times();
        times.setNome("Palmeiras");
        times.setSigla("PAL");
        times.setEstado("SP");

        assertEquals("Palmeiras", times.getNome());
        assertEquals("PAL", times.getSigla());
        assertEquals("SP", times.getEstado());
    }
}