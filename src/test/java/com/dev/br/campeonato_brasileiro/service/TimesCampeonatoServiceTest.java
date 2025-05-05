package com.dev.br.campeonato_brasileiro.service;

import com.dev.br.campeonato_brasileiro.model.Times;
import com.dev.br.campeonato_brasileiro.repository.TimesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TimesCampeonatoServiceTest {

    @Mock
    private TimesRepository timesRepository;

    @InjectMocks
    private TimesCampeonatoService timesCampeonatoService;

    private List<Times> equipes;

    @BeforeEach
    void setUp() {
        equipes = List.of(
                new Times("Time A"), new Times("Time B"), new Times("Time C"), new Times("Time D"),
                new Times("Time E"), new Times("Time F"), new Times("Time G"), new Times("Time H"),
                new Times("Time I"), new Times("Time J"), new Times("Time K"), new Times("Time L"),
                new Times("Time M"), new Times("Time N"), new Times("Time O"), new Times("Time P"),
                new Times("Time Q"), new Times("Time R"), new Times("Time S"), new Times("Time T")
        );
    }

    @Test
    void testInserirEquipesComQuantidadeCorreta() {
        timesCampeonatoService.inserirEquipes(equipes);
        verify(timesRepository, times(1)).saveAll(equipes);
    }

    @Test
    void testInserirEquipesComQuantidadeIncorreta() {
        List<Times> equipesInvalidas = List.of(new Times("Time Único"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            timesCampeonatoService.inserirEquipes(equipesInvalidas);
        });

        assertEquals("A lista deve conter exatamente 20 equipes.", exception.getMessage());
        verify(timesRepository, never()).saveAll(any());
    }

    @Test
    void testListarEquipes() {
        when(timesRepository.findAll()).thenReturn(equipes);

        List<Times> result = timesCampeonatoService.listarEquipes();

        assertEquals(20, result.size());
        assertEquals("Time A", result.get(0).getNome());
        verify(timesRepository, times(1)).findAll();
    }
}
