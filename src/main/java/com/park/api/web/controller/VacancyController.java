package com.park.api.web.controller;

import com.park.api.entities.Vacancy;
import com.park.api.service.VacancyService;
import com.park.api.web.dto.VacancyCreateDTO;
import com.park.api.web.dto.VacancyResponseDTO;
import com.park.api.web.dto.mapper.VacancyMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vacancy")
public class VacancyController {

    private final VacancyService vacancyService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid VacancyCreateDTO createDTO){
        Vacancy vacancy = VacancyMapper.toVacancy(createDTO);
        vacancyService.save(vacancy);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{code}")
                .buildAndExpand(vacancy.getCode())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VacancyResponseDTO> getByCode(@PathVariable String code){
        Vacancy vacancy = vacancyService.findByCode(code);
        return ResponseEntity.ok(VacancyMapper.toDTO(vacancy));
    }
}
