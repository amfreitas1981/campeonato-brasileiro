package com.dev.br.campeonato_brasileiro.controller;

import com.dev.br.campeonato_brasileiro.dto.ClassificacaoDTO;
import com.dev.br.campeonato_brasileiro.model.Partidas;
import com.dev.br.campeonato_brasileiro.model.Times;
import com.dev.br.campeonato_brasileiro.repository.ClassificacaoRepository;
import com.dev.br.campeonato_brasileiro.repository.PartidasRepository;
import com.dev.br.campeonato_brasileiro.repository.TimesRepository;
import com.dev.br.campeonato_brasileiro.service.ClassificacaoService;
import com.dev.br.campeonato_brasileiro.service.ExportacaoService;
import com.dev.br.campeonato_brasileiro.service.GeradorDePartidasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CampeonatoControllerTest {

    @Mock
    private TimesRepository timesRepository;

    @Mock
    private PartidasRepository partidasRepository;

    @Mock
    private ClassificacaoRepository classificacaoRepository;

    @Mock
    private GeradorDePartidasService geradorDePartidasService;

    @Mock
    private ClassificacaoService classificacaoService;

    @Mock
    private ExportacaoService exportacaoService;

    @InjectMocks
    private CampeonatoController campeonatoController;

    private List<Partidas> partidasMock;
    private List<Times> timesMock;
    private List<ClassificacaoDTO> classificacaoMock;
    private ByteArrayInputStream excelStream;

    @BeforeEach
    void setUp() {
        // Configurar dados de teste
        Times time1 = new Times(1L, "Flamengo", "FLA", "RJ");
        Times time2 = new Times(2L, "São Paulo", "SAO", "SP");

        partidasMock = Arrays.asList(
                new Partidas(1L, time1, time2, 3, 1, 1, LocalDate.now()),
                new Partidas(2L, time2, time1, 0, 2, 1, LocalDate.now().plusDays(7))
        );

        timesMock = Arrays.asList(time1, time2);

        classificacaoMock = Arrays.asList(
                new ClassificacaoDTO(1, "Flamengo", 10, 5, 3, 1, 1, 8, 3, 5),
                new ClassificacaoDTO(2, "São Paulo", 8, 5, 2, 2, 1, 6, 4, 2)
        );

        // Criar um stream vazio para simular o Excel
        excelStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
    }

    @Test
    void listarPartidasPorRodada_DeveRetornarPartidasDaRodada() {
        // Arrange
        when(partidasRepository.findByRound(1)).thenReturn(partidasMock);

        // Act
        ResponseEntity<List<Partidas>> response = campeonatoController.listarPartidasPorRodada(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(partidasMock, response.getBody());
        verify(partidasRepository).findByRound(1);
    }

    @Test
    void resetarCampeonato_DeveLimparRepositorios() {
        // Act
        ResponseEntity<String> response = campeonatoController.resetarCampeonato();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Campeonato reiniciado com sucesso.", response.getBody());
        verify(classificacaoRepository).deleteAll();
        verify(partidasRepository).deleteAll();
    }

    @Test
    void exportarClassificacaoParaExcel_DeveRetornarArquivoExcel() {
        // Arrange
        when(exportacaoService.exportarClassificacaoParaExcel()).thenReturn(excelStream);

        // Act
        ResponseEntity<byte[]> response = campeonatoController.exportarClassificacaoParaExcel();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("attachment; filename=classificacao.xlsx",
                response.getHeaders().getFirst("Content-Disposition"));
        verify(exportacaoService).exportarClassificacaoParaExcel();
    }

    @Test
    void getClassificacao_DeveRetornarClassificacaoCompleta() {
        // Arrange
        when(classificacaoService.getClassificacaoOrdenadaDTO()).thenReturn(classificacaoMock);

        // Act
        ResponseEntity<List<ClassificacaoDTO>> response = campeonatoController.getClassificacao();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(classificacaoMock, response.getBody());
        verify(classificacaoService).getClassificacaoOrdenadaDTO();
    }

    @Test
    void getLibertadores_DeveRetornarTimesClassificadosParaLibertadores() {
        // Arrange
        List<ClassificacaoDTO> libertadoresMock = Arrays.asList(classificacaoMock.get(0));
        when(classificacaoService.getClassificacaoDTOByInterval(0, 6)).thenReturn(libertadoresMock);

        // Act
        ResponseEntity<List<ClassificacaoDTO>> response = campeonatoController.getLibertadores();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(libertadoresMock, response.getBody());
        verify(classificacaoService).getClassificacaoDTOByInterval(0, 6);
    }

    @Test
    void getSulAmericana_DeveRetornarTimesClassificadosParaSulAmericana() {
        // Arrange
        List<ClassificacaoDTO> sulamericanaMock = Arrays.asList(classificacaoMock.get(1));
        when(classificacaoService.getClassificacaoDTOByInterval(6, 12)).thenReturn(sulamericanaMock);

        // Act
        ResponseEntity<List<ClassificacaoDTO>> response = campeonatoController.getSulAmericana();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sulamericanaMock, response.getBody());
        verify(classificacaoService).getClassificacaoDTOByInterval(6, 12);
    }

    @Test
    void getRebaixamento_DeveRetornarTimesRebaixados() {
        // Arrange
        List<ClassificacaoDTO> rebaixadosMock = Arrays.asList(classificacaoMock.get(1));
        when(classificacaoService.getClassificacaoDTOByInterval(16, 20)).thenReturn(rebaixadosMock);

        // Act
        ResponseEntity<List<ClassificacaoDTO>> response = campeonatoController.getRebaixamento();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rebaixadosMock, response.getBody());
        verify(classificacaoService).getClassificacaoDTOByInterval(16, 20);
    }

    @Test
    void gerarPartidas_DeveGerarEPersistirPartidas() {
        // Arrange
        when(timesRepository.findAll()).thenReturn(timesMock);
        when(geradorDePartidasService.gerarPartidas(timesMock)).thenReturn(partidasMock);

        // Act
        ResponseEntity<String> response = campeonatoController.gerarPartidas();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Partidas geradas e salvas com sucesso.", response.getBody());
        verify(timesRepository).findAll();
        verify(geradorDePartidasService).gerarPartidas(timesMock);
        verify(partidasRepository).saveAll(partidasMock);
    }

    @Test
    void listarPartidas_DeveRetornarTodasPartidas() {
        // Arrange
        when(partidasRepository.findAll()).thenReturn(partidasMock);

        // Act
        ResponseEntity<List<Partidas>> response = campeonatoController.listarPartidas();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(partidasMock, response.getBody());
        verify(partidasRepository).findAll();
    }

    @Test
    void simularCampeonato_ComPartidasJaExistentes_DeveSimularTodasRodadas() {
        // Arrange
        when(partidasRepository.findAll()).thenReturn(partidasMock);

        // Act
        ResponseEntity<String> response = campeonatoController.simularCampeonato();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Simulação concluída!", response.getBody());
        verify(partidasRepository).findAll();
        verify(geradorDePartidasService, times(38)).simularRodada(anyInt());
        verify(geradorDePartidasService, never()).gerarPartidas(any());
    }

    @Test
    void simularCampeonato_SemPartidasExistentes_DeveGerarPartidasESimular() {
        // Arrange
        when(partidasRepository.findAll()).thenReturn(new ArrayList<>());
        when(timesRepository.findAll()).thenReturn(timesMock);
        when(geradorDePartidasService.gerarPartidas(timesMock)).thenReturn(partidasMock);

        // Act
        ResponseEntity<String> response = campeonatoController.simularCampeonato();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Simulação concluída!", response.getBody());
        verify(partidasRepository).findAll();
        verify(timesRepository).findAll();
        verify(geradorDePartidasService).gerarPartidas(timesMock);
        verify(partidasRepository).saveAll(partidasMock);
        verify(geradorDePartidasService, times(38)).simularRodada(anyInt());
    }
}
