package com.fredyhg.destiny2jobs.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "card_tb")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Integer kills;

    private Double kda;

    private String winrate;

    private Integer elo;


}
