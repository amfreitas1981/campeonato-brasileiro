package com.dev.br.campeonato_brasileiro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Partidas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Times timesCasa;

    @ManyToOne
    private Times timesFora;

    private Integer golsCasa;
    private Integer golsFora;
    private int rodada;

    public Partidas(Times timesCasa, Times timesFora, int rodada) {
        this.timesCasa = timesCasa;
        this.timesFora = timesFora;
        this.rodada = rodada;
    }
}