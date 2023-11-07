package com.fredyhg.destiny2jobs.controllers.impls;

import com.fredyhg.destiny2jobs.controllers.CardController;
import com.fredyhg.destiny2jobs.models.dtos.card.CardPostDto;
import com.fredyhg.destiny2jobs.utils.models.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/card")
@RequiredArgsConstructor
public class CardControllerImpl implements CardController {

    @PostMapping("/create")
    @Override
    public ResponseEntity<ResponseMessage> createCard(CardPostDto card) {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Card created successfully"));
    }

}
