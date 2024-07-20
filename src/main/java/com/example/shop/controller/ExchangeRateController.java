package com.example.shop.controller;

import com.example.shop.dto.ResponseDTO;
import com.example.shop.service.BankService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class ExchangeRateController {    @Autowired
    BankService bankService;



    @GetMapping(path="/fx")
    public ResponseEntity<ResponseDTO> getConversion()
    {
        try {
            return new ResponseEntity<ResponseDTO>(bankService.fetchCurrency(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<ResponseDTO>(new ResponseDTO(), HttpStatus.OK);
        }
    }

    @GetMapping(path="/fx/{targetCurrency}")
    public ResponseEntity<ResponseDTO> targertCurrency(@PathVariable(value="targetCurrency") String targetCurrency)
    {
        return new ResponseEntity<ResponseDTO>(bankService.fetchTargetCurrency(targetCurrency,3), HttpStatus.OK);
    }

}
