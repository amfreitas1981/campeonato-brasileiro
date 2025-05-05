package com.dev.br.campeonato_brasileiro.controller;

import com.dev.br.campeonato_brasileiro.model.Times;
import com.dev.br.campeonato_brasileiro.service.TimesCampeonatoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TimesCampeonatoControllerTest {

    @Mock
    private TimesCampeonatoService timesCampeonatoService;

    @InjectMocks
    private TimesCampeonatoController timesCampeonatoController;

    @Test
    void testInserirEquipes() {
        List<Times> equipes = List.of(
                new Times("Time A"), new Times("Time B"), new Times("Time C"), new Times("Time D"),
                new Times("Time E"), new Times("Time F"), new Times("Time G"), new Times("Time H"),
                new Times("Time I"), new Times("Time J"), new Times("Time K"), new Times("Time L"),
                new Times("Time M"), new Times("Time N"), new Times("Time O"), new Times("Time P"),
                new Times("Time Q"), new Times("Time R"), new Times("Time S"), new Times("Time T")
        );

        ResponseEntity<String> response = timesCampeonatoController.inserirEquipes(equipes);

        assertEquals("Equipes inseridas com sucesso.", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(timesCampeonatoService, times(1)).inserirEquipes(equipes);
    }

    @Test
    void testListarEquipes() {
        List<Times> equipes = List.of(new Times("Time A"), new Times("Time B"));
        when(timesCampeonatoService.listarEquipes()).thenReturn(equipes);

        ResponseEntity<List<Times>> response = timesCampeonatoController.listarEquipes();

        assertEquals(2, response.getBody().size());
        assertEquals("Time A", response.getBody().get(0).getNome());
        assertEquals(200, response.getStatusCodeValue());
        verify(timesCampeonatoService, times(1)).listarEquipes();
    }
}
