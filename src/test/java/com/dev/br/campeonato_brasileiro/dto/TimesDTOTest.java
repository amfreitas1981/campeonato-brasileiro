package com.dev.br.campeonato_brasileiro.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TimesDTOTest {

    @Test
    void testTimesDTO() {
        Long id = 1L;
        String nome = "Flamengo";
        String sigla = "FLA";
        String estado = "RJ";

        TimesDTO timesDTO = new TimesDTO(id, nome, sigla, estado);

        assertEquals(id, timesDTO.id());
        assertEquals(nome, timesDTO.nome());
        assertEquals(sigla, timesDTO.sigla());
        assertEquals(estado, timesDTO.estado());
    }
}
