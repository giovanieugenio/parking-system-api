package com.park.api.web.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingCreateDTO {

    @NotBlank
    private String brand;

    @NotBlank
    @Size(min = 8, max = 8)
    @Pattern(regexp = "[A-Z]{3}-[0-9]{4}", message = "The vehicle's license plate must follow the following pattern: 'XXX-0000'")
    private String plate;

    @NotBlank
    private String model;

    @NotBlank
    private String color;

    @NotBlank
    @Size(min = 11, max = 11)
    @CPF
    private String clientCpf;
}
