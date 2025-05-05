package com.dev.br.campeonato_brasileiro.service;

import com.dev.br.campeonato_brasileiro.dto.ClassificacaoDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportacaoService {

    private final ClassificacaoService classificacaoService;

    public ByteArrayInputStream exportarClassificacaoParaExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Classificação");

            // Cabeçalhos
            Row header = sheet.createRow(0);
            String[] colunas = {
                    "Posição", "Time", "Pontos", "Jogos", "Vitórias", "Empates", "Derrotas", "Gols Pró", "Gols Contra", "Saldo de Gols"
            };

            for (int i = 0; i < colunas.length; i++) {
                header.createCell(i).setCellValue(colunas[i]);
            }

            // Dados
            List<ClassificacaoDTO> classificacoes = classificacaoService.getClassificacaoOrdenadaDTO();
            int linha = 1;
            for (ClassificacaoDTO c : classificacoes) {
                Row row = sheet.createRow(linha++);
                row.createCell(0).setCellValue(c.posicao());
                row.createCell(1).setCellValue(c.times());
                row.createCell(2).setCellValue(c.pontos());
                row.createCell(3).setCellValue(c.round());
                row.createCell(4).setCellValue(c.vitorias());
                row.createCell(5).setCellValue(c.empates());
                row.createCell(6).setCellValue(c.derrotas());
                row.createCell(7).setCellValue(c.golsPro());
                row.createCell(8).setCellValue(c.golsContra());
                row.createCell(9).setCellValue(c.saldoGols());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao exportar Excel", e);
        }
    }
}
