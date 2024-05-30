package com.park.api.web.dto.mapper;

import com.park.api.entities.Vacancy;
import com.park.api.web.dto.VacancyCreateDTO;
import com.park.api.web.dto.VacancyResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VacancyMapper {

    public static Vacancy toVacancy(VacancyCreateDTO createDTO){
        return new ModelMapper().map(createDTO, Vacancy.class);
    }

    public static VacancyResponseDTO toDTO(Vacancy vacancy){
        return new ModelMapper().map(vacancy, VacancyResponseDTO.class);
    }
}
