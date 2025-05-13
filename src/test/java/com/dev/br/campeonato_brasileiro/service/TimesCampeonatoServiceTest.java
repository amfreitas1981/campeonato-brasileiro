package com.dev.br.campeonato_brasileiro.service;

import com.dev.br.campeonato_brasileiro.dto.TimesDTO;
import com.dev.br.campeonato_brasileiro.model.Times;
import com.dev.br.campeonato_brasileiro.repository.TimesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimesCampeonatoServiceTest {

    @Mock
    private TimesRepository repository;

    @InjectMocks
    private TimesCampeonatoService service;

    private List<TimesDTO> equipesDTO;

    @BeforeEach
    void setUp() {
        equipesDTO = List.of(
                new TimesDTO(null, "Time A", "TA", "SP"),
                new TimesDTO(null, "Time B", "TB", "RJ"),
                new TimesDTO(null, "Time C", "TC", "MG"),
                new TimesDTO(null, "Time D", "TD", "RS"),
                new TimesDTO(null, "Time E", "TE", "PE"),
                new TimesDTO(null, "Time F", "TF", "BA"),
                new TimesDTO(null, "Time G", "TG", "SC"),
                new TimesDTO(null, "Time H", "TH", "GO"),
                new TimesDTO(null, "Time I", "TI", "CE"),
                new TimesDTO(null, "Time J", "TJ", "PR"),
                new TimesDTO(null, "Time K", "TK", "AL"),
                new TimesDTO(null, "Time L", "TL", "MA"),
                new TimesDTO(null, "Time M", "TM", "PB"),
                new TimesDTO(null, "Time N", "TN", "SE"),
                new TimesDTO(null, "Time O", "TO", "DF"),
                new TimesDTO(null, "Time P", "TP", "AM"),
                new TimesDTO(null, "Time Q", "TQ", "RR"),
                new TimesDTO(null, "Time R", "TR", "AP"),
                new TimesDTO(null, "Time S", "TS", "RO"),
                new TimesDTO(null, "Time T", "TT", "MS")
        );
    }

    @Test
    void inserirEquipes_DeveSalvarEquipesCorretamente() {
        when(repository.saveAll(any())).thenReturn(List.of());

        service.inserirEquipes(equipesDTO);

        verify(repository, times(1)).deleteAll();
        verify(repository, times(1)).saveAll(any());
    }

    @Test
    void inserirEquipes_DeveLancarExcecao_QuandoListaForInvalida() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> service.inserirEquipes(List.of()));
        assertEquals("A lista deve conter exatamente 20 equipes.", thrown.getMessage());
    }

    @Test
    void inserirEquipes_DeveLancarExcecao_QuandoHouverTimesDuplicados() {
        List<TimesDTO> listaDuplicada = List.of(
                new TimesDTO(null, "Time A", "TA", "SP"),
                new TimesDTO(null, "Time B", "TB", "RJ"),
                new TimesDTO(null, "Time C", "TC", "MG"),
                new TimesDTO(null, "Time D", "TD", "RS"),
                new TimesDTO(null, "Time E", "TE", "PE"),
                new TimesDTO(null, "Time F", "TF", "BA"),
                new TimesDTO(null, "Time G", "TG", "SC"),
                new TimesDTO(null, "Time H", "TH", "GO"),
                new TimesDTO(null, "Time I", "TI", "CE"),
                new TimesDTO(null, "Time J", "TJ", "PR"),
                new TimesDTO(null, "Time K", "TK", "AL"),
                new TimesDTO(null, "Time L", "TL", "MA"),
                new TimesDTO(null, "Time M", "TM", "PB"),
                new TimesDTO(null, "Time N", "TN", "SE"),
                new TimesDTO(null, "Time O", "TO", "DF"),
                new TimesDTO(null, "Time P", "TP", "AM"),
                new TimesDTO(null, "Time Q", "TQ", "RR"),
                new TimesDTO(null, "Time R", "TR", "AP"),
                new TimesDTO(null, "Time S", "TS", "RO"),
                new TimesDTO(null, "Time A", "TA", "SP") // Time duplicado
        );

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> service.inserirEquipes(listaDuplicada));
        assertEquals("Erro ao inserir equipes. Tente novamente. Existem times duplicados na lista.", thrown.getMessage());
    }

    @Test
    void inserirEquipes_DeveLancarExcecao_QuandoOcorrerErroDeIntegridadeDeDados() {
        doThrow(new DataIntegrityViolationException("Chave duplicada"))
                .when(repository).saveAll(any());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> service.inserirEquipes(equipesDTO));
        assertTrue(thrown.getMessage().contains("Erro de integridade de dados ao inserir equipes."));
    }

    @Test
    void inserirEquipes_DeveLancarExcecao_QuandoOcorrerErroGenerico() {
        doThrow(new RuntimeException("Erro inesperado"))
                .when(repository).saveAll(any());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> service.inserirEquipes(equipesDTO));
        assertTrue(thrown.getMessage().contains("Erro ao inserir equipes."));
    }

    @Test
    void listarEquipes_DeveRetornarListaDeTimes() {
        List<Times> mockTimes = List.of(new Times("Time X", "TX", "SP"));
        when(repository.findAll()).thenReturn(mockTimes);

        List<Times> result = service.listarEquipes();

        assertEquals(1, result.size());
        assertEquals("Time X", result.get(0).getNome());
    }

    @Test
    void listarEquipes_DeveRetornarListaVazia_QuandoNaoExistiremTimes() {
        when(repository.findAll()).thenReturn(List.of());

        List<Times> result = service.listarEquipes();

        assertTrue(result.isEmpty());
    }
}
