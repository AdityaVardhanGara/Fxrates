package com.example.shop.dto;

import com.example.shop.model.ExchangeRateDummy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    List<ExchangeRateDTO> object;
}
