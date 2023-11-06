package com.fredyhg.destiny2jobs.controllers;

import com.fredyhg.destiny2jobs.models.dtos.card.CardPostDto;
import com.fredyhg.destiny2jobs.utils.models.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface CardController {

    @Operation(summary = "Create new account", description = "Worker can make this request.", tags = "WORKER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card created successfully"),
            @ApiResponse(responseCode = "409", description = "Char already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    ResponseEntity<ResponseMessage> createCard(CardPostDto card);
}
