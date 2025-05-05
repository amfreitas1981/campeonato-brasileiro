package com.dev.br.campeonato_brasileiro.dto;

import java.time.LocalDate;

public record PartidasDTO(
        String timesCasa,
        String timesFora,
        Integer golsCasa,
        Integer golsVisitante,
        int rodada,
        LocalDate dataPartida
) {}
