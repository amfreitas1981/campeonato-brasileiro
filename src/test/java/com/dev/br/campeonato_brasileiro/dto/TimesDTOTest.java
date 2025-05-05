package com.dev.br.campeonato_brasileiro.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TimesDTOTest {

    @Test
    public void testCriacaoDTO() {
        String nome = "Vasco";
        String sigla = "VAS";
        String estado = "RJ";

        TimesDTO timesDTO = new TimesDTO(nome, sigla, estado);

        assertEquals(nome, timesDTO.nome());
        assertEquals(sigla, timesDTO.sigla());
        assertEquals(estado, timesDTO.estado());
    }

    @Test
    public void testIgualdade() {
        TimesDTO timesDTO1 = new TimesDTO("Corinthians", "COR", "SP");
        TimesDTO timesDTO2 = new TimesDTO("Corinthians", "COR", "SP");
        TimesDTO timesDTO3 = new TimesDTO("Santos", "SAN", "SP");

        // Teste de igualdade reflexiva
        assertEquals(timesDTO1, timesDTO1);

        // Teste de igualdade simétrica
        assertEquals(timesDTO1, timesDTO2);
        assertEquals(timesDTO2, timesDTO1);

        // Teste de desigualdade
        assertNotEquals(timesDTO1, timesDTO3);

        // Teste com null
        assertNotEquals(timesDTO1, null);

        // Teste com outro tipo
        assertNotEquals(timesDTO1, "String");
    }

    @Test
    public void testHashCode() {
        TimesDTO timesDTO1 = new TimesDTO("Fluminense", "FLU", "RJ");
        TimesDTO timesDTO2 = new TimesDTO("Fluminense", "FLU", "RJ");

        // Objetos iguais devem ter mesmo hashCode
        assertEquals(timesDTO1.hashCode(), timesDTO2.hashCode());
    }

    @Test
    public void testToString() {
        String nome = "Botafogo";
        String sigla = "BOT";
        String estado = "RJ";

        TimesDTO timesDTO = new TimesDTO(nome, sigla, estado);
        String toStringResult = timesDTO.toString();

        // Verificando se o toString contém todos os campos
        assertTrue(toStringResult.contains(nome));
        assertTrue(toStringResult.contains(sigla));
        assertTrue(toStringResult.contains(estado));
    }
}
