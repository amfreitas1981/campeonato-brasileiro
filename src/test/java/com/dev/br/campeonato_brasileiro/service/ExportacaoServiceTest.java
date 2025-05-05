package com.dev.br.campeonato_brasileiro.service;

import com.dev.br.campeonato_brasileiro.dto.ClassificacaoDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ExportacaoServiceTest {

    @Mock
    private ClassificacaoService classificacaoService;

    @InjectMocks
    private ExportacaoService exportacaoService;

    private List<ClassificacaoDTO> mockClassificacao;

    @BeforeEach
    void setUp() {
        mockClassificacao = List.of(
                new ClassificacaoDTO(1, "Time A", 30, 15, 9, 3, 3, 25, 12, 13),
                new ClassificacaoDTO(2, "Time B", 28, 15, 8, 4, 3, 22, 14, 8)
        );
    }

    @Test
    void exportarClassificacaoParaExcel_deveGerarArquivoExcelComDadosCorretos() throws Exception {
        when(classificacaoService.getClassificacaoOrdenadaDTO()).thenReturn(mockClassificacao);

        ByteArrayInputStream inputStream = exportacaoService.exportarClassificacaoParaExcel();
        assertNotNull(inputStream);

        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        // Verifica cabeçalhos
        Row header = sheet.getRow(0);
        assertEquals("Posição", header.getCell(0).getStringCellValue());
        assertEquals("Time", header.getCell(1).getStringCellValue());
        assertEquals("Pontos", header.getCell(2).getStringCellValue());

        // Verifica dados da primeira linha
        Row row1 = sheet.getRow(1);
        assertEquals(1, (int) row1.getCell(0).getNumericCellValue());
        assertEquals("Time A", row1.getCell(1).getStringCellValue());
        assertEquals(30, (int) row1.getCell(2).getNumericCellValue());

        // Verifica dados da segunda linha
        Row row2 = sheet.getRow(2);
        assertEquals(2, (int) row2.getCell(0).getNumericCellValue());
        assertEquals("Time B", row2.getCell(1).getStringCellValue());
        assertEquals(28, (int) row2.getCell(2).getNumericCellValue());

        workbook.close();
    }

    @Test
    void exportarClassificacaoParaExcel_quandoErroDeveLancarExcecao() {
        when(classificacaoService.getClassificacaoOrdenadaDTO()).thenThrow(new RuntimeException("Erro simulado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            exportacaoService.exportarClassificacaoParaExcel();
        });

        assertTrue(exception.getMessage().contains("Erro ao exportar Excel"));
    }
}
