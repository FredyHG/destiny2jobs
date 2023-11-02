package com.fredyhg.destiny2jobs.models.dtos.card;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardPostDto {

    private Integer kills;
    private Double kda;
    private String winrate;
    private Integer elo;
}
