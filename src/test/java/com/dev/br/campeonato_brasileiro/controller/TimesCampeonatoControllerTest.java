package com.dev.br.campeonato_brasileiro.controller;

import com.dev.br.campeonato_brasileiro.dto.TimesDTO;
import com.dev.br.campeonato_brasileiro.model.Times;
import com.dev.br.campeonato_brasileiro.service.TimesCampeonatoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TimesCampeonatoControllerTest {

    @Mock
    private TimesCampeonatoService service;

    @InjectMocks
    private TimesCampeonatoController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void inserirEquipes_DeveRetornarOk() {
        List<TimesDTO> equipes = List.of(
                new TimesDTO(1L, "Flamengo", "FLA", "RJ"),
                new TimesDTO(2L, "Palmeiras", "PAL", "SP")
        );

        doNothing().when(service).inserirEquipes(equipes);

        ResponseEntity<?> response = controller.inserirEquipes(equipes);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Equipes inseridas com sucesso.", response.getBody());
    }

    @Test
    void inserirEquipes_DeveRetornarBadRequestParaListaInvalida() {
        List<TimesDTO> equipes = List.of(new TimesDTO(1L, "Flamengo", "FLA", "RJ")); // Apenas um time

        doThrow(new IllegalArgumentException("A lista deve conter exatamente 20 equipes."))
                .when(service).inserirEquipes(equipes);

        ResponseEntity<?> response = controller.inserirEquipes(equipes);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("A lista deve conter exatamente 20 equipes.", response.getBody());
    }

    @Test
    void inserirEquipes_DeveRetornarInternalServerErrorParaErroGenerico() {
        List<TimesDTO> equipes = List.of(
                new TimesDTO(1L, "Flamengo", "FLA", "RJ"),
                new TimesDTO(2L, "Palmeiras", "PAL", "SP")
        );

        doThrow(new RuntimeException("Erro interno inesperado"))
                .when(service).inserirEquipes(equipes);

        ResponseEntity<?> response = controller.inserirEquipes(equipes);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Erro ao inserir equipes: Erro interno inesperado", response.getBody());
    }

    @Test
    void listarEquipes_DeveRetornarListaDeTimes() {
        List<Times> equipes = List.of(new Times(1L, "Flamengo", "FLA", "RJ"));

        when(service.listarEquipes()).thenReturn(equipes);

        ResponseEntity<List<Times>> response = controller.listarEquipes();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(equipes, response.getBody());
    }

    @Test
    void listarEquipes_DeveRetornarListaVazia() {
        when(service.listarEquipes()).thenReturn(List.of());

        ResponseEntity<List<Times>> response = controller.listarEquipes();

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }
}
