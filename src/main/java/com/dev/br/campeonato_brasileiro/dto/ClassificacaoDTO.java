package com.dev.br.campeonato_brasileiro.dto;

public record ClassificacaoDTO(
        int posicao,
        String times,
        int pontos,
        int round,
        int vitorias,
        int empates,
        int derrotas,
        int golsPro,
        int golsContra,
        int saldoGols
) {}
