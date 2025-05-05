package com.dev.br.campeonato_brasileiro.controller;

import com.dev.br.campeonato_brasileiro.dto.ClassificacaoDTO;
import com.dev.br.campeonato_brasileiro.service.ClassificacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class ClassificacaoControllerTest {

    @Mock
    private ClassificacaoService classificacaoService;

    @InjectMocks
    private ClassificacaoController classificacaoController;

    private MockMvc mockMvc;
    private List<ClassificacaoDTO> mockClassificacaoList;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(classificacaoController).build();

        // Criando dados simulados para testes
        mockClassificacaoList = Arrays.asList(
                new ClassificacaoDTO(1, "Flamengo", 70, 30, 21, 7, 2, 60, 20, 40),
                new ClassificacaoDTO(2, "Palmeiras", 65, 30, 19, 8, 3, 55, 22, 33),
                new ClassificacaoDTO(3, "São Paulo", 62, 30, 18, 8, 4, 50, 25, 25),
                new ClassificacaoDTO(4, "Atlético-MG", 59, 30, 17, 8, 5, 48, 26, 22),
                new ClassificacaoDTO(5, "Fluminense", 56, 30, 16, 8, 6, 45, 28, 17),
                new ClassificacaoDTO(6, "Internacional", 53, 30, 15, 8, 7, 40, 30, 10),
                new ClassificacaoDTO(7, "Corinthians", 50, 30, 14, 8, 8, 38, 32, 6),
                new ClassificacaoDTO(8, "Grêmio", 47, 30, 13, 8, 9, 36, 33, 3),
                new ClassificacaoDTO(9, "Santos", 44, 30, 12, 8, 10, 35, 35, 0),
                new ClassificacaoDTO(10, "Athletico-PR", 41, 30, 11, 8, 11, 32, 36, -4),
                new ClassificacaoDTO(11, "Botafogo", 38, 30, 10, 8, 12, 30, 38, -8),
                new ClassificacaoDTO(12, "Bragantino", 35, 30, 9, 8, 13, 28, 40, -12),
                new ClassificacaoDTO(13, "Fortaleza", 32, 30, 8, 8, 14, 26, 42, -16),
                new ClassificacaoDTO(14, "Bahia", 29, 30, 7, 8, 15, 24, 44, -20),
                new ClassificacaoDTO(15, "Ceará", 26, 30, 6, 8, 16, 22, 46, -24),
                new ClassificacaoDTO(16, "Vasco", 23, 30, 5, 8, 17, 20, 48, -28),
                new ClassificacaoDTO(17, "Goiás", 20, 30, 4, 8, 18, 18, 50, -32),
                new ClassificacaoDTO(18, "Coritiba", 17, 30, 3, 8, 19, 16, 52, -36),
                new ClassificacaoDTO(19, "Avaí", 14, 30, 2, 8, 20, 14, 54, -40),
                new ClassificacaoDTO(20, "Juventude", 11, 30, 1, 8, 21, 12, 56, -44)
        );
    }

    @Test
    public void testGetClassificacaoGeral() throws Exception {
        when(classificacaoService.listarClassificacaoGeral()).thenReturn(mockClassificacaoList);

        mockMvc.perform(get("/api/classification/general")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(20)))
                .andExpect(jsonPath("$[0].posicao").value(1))
                .andExpect(jsonPath("$[0].times").value("Flamengo"))
                .andExpect(jsonPath("$[0].pontos").value(70));

        verify(classificacaoService, times(1)).listarClassificacaoGeral();
        verifyNoMoreInteractions(classificacaoService);
    }

    @Test
    public void testGetLibertadores() throws Exception {
        List<ClassificacaoDTO> libertadoresList = mockClassificacaoList.subList(0, 6);
        when(classificacaoService.getLibertadores()).thenReturn(libertadoresList);

        mockMvc.perform(get("/api/classification/libertadores")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].times").value("Flamengo"))
                .andExpect(jsonPath("$[5].times").value("Internacional"));

        verify(classificacaoService, times(1)).getLibertadores();
        verifyNoMoreInteractions(classificacaoService);
    }

    @Test
    public void testGetSulAmericana() throws Exception {
        List<ClassificacaoDTO> sulAmericanaList = mockClassificacaoList.subList(6, 12);
        when(classificacaoService.getSulAmericana()).thenReturn(sulAmericanaList);

        mockMvc.perform(get("/api/classification/sulamericana")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].times").value("Corinthians"))
                .andExpect(jsonPath("$[5].times").value("Bragantino"));

        verify(classificacaoService, times(1)).getSulAmericana();
        verifyNoMoreInteractions(classificacaoService);
    }

    @Test
    public void testGetRebaixamento() throws Exception {
        List<ClassificacaoDTO> rebaixamentoList = mockClassificacaoList.subList(16, 20);
        when(classificacaoService.getRebaixamento()).thenReturn(rebaixamentoList);

        mockMvc.perform(get("/api/classification/downgrade")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].times").value("Goiás"))
                .andExpect(jsonPath("$[3].times").value("Juventude"));

        verify(classificacaoService, times(1)).getRebaixamento();
        verifyNoMoreInteractions(classificacaoService);
    }
}
