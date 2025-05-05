package com.dev.br.campeonato_brasileiro.repository;

import com.dev.br.campeonato_brasileiro.model.Classificacao;
import com.dev.br.campeonato_brasileiro.model.Times;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassificacaoRepository extends JpaRepository<Classificacao, Long> {
    Optional<Classificacao> findByTimes(Times times);
    List<Classificacao> findAllByOrderByPontosDescVitoriasDescSaldoGolsDescGolsProDesc();
}