package com.park.api.repositories.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ClientVacancyProjection {

    String getBrand();
    String getPlate();
    String getModel();
    String gerColor();
    String getClientCpf();
    String getReceipt();

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getEntryDate();

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getExitDate();

    String getVacancyCode();
    BigDecimal getPrice();
    BigDecimal getDiscount();
}
