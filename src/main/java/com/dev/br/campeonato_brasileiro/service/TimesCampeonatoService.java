package com.dev.br.campeonato_brasileiro.service;

import com.dev.br.campeonato_brasileiro.dto.TimesDTO;
import com.dev.br.campeonato_brasileiro.model.Times;
import com.dev.br.campeonato_brasileiro.repository.TimesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimesCampeonatoService {

    @Autowired
    private TimesRepository repository;

    @Transactional
    public void inserirEquipes(List<TimesDTO> equipesDTO) {
        if (equipesDTO == null || equipesDTO.size() != 20) {
            throw new IllegalArgumentException("A lista deve conter exatamente 20 equipes.");
        }

        try {
            // Verificar se há duplicatas pelo nome
            Map<String, Long> countByNome = equipesDTO.stream()
                    .collect(Collectors.groupingBy(TimesDTO::nome, Collectors.counting()));

            boolean hasDuplicates = countByNome.values().stream().anyMatch(count -> count > 1);
            if (hasDuplicates) {
                throw new IllegalArgumentException("Existem times duplicados na lista.");
            }

            // Limpar a tabela atual (opcional, dependendo da regra de negócio)
            repository.deleteAll();

            // Converter DTO para entidade (ignorando o ID do DTO)
            List<Times> equipes = equipesDTO.stream()
                    .map(dto -> new Times(dto.nome(), dto.sigla(), dto.estado()))
                    .collect(Collectors.toList());

            // Salvar todas as equipes
            repository.saveAll(equipes);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Erro de integridade de dados ao inserir equipes. " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir equipes. Tente novamente. " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<Times> listarEquipes() {
        return repository.findAll();
    }
}
