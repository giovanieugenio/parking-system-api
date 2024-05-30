package com.park.api.web.dto.mapper;

import com.park.api.entities.ClientVacancy;
import com.park.api.web.dto.ParkingCreateDTO;
import com.park.api.web.dto.ParkingResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientVacancyMapper {

    public static ClientVacancy toClientVacancy(ParkingCreateDTO createDTO){
        return new ModelMapper().map(createDTO, ClientVacancy.class);
    }
    public static ParkingResponseDTO toDTO(ClientVacancy clientVacancy){
        return new ModelMapper().map(clientVacancy, ParkingResponseDTO.class);
    }
}
