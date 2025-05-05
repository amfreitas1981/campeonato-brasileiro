package com.dev.br.campeonato_brasileiro.repository;

import com.dev.br.campeonato_brasileiro.model.Partidas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartidasRepository extends JpaRepository<Partidas, Long> {
    List<Partidas> findByRound(int round);
}