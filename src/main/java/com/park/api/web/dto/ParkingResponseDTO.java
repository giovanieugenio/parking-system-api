package com.park.api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingResponseDTO {

    private String brand;
    private String plate;
    private String model;
    private String color;
    private String clientCpf;
    private String receipt;
    private LocalDateTime entryDate;
    private LocalDateTime exitDate;
    private String vacancyCode;
    private BigDecimal price;
    private BigDecimal discount;

}