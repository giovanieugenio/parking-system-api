package com.park.api.web.controller;

import com.park.api.entities.Vacancy;
import com.park.api.service.VacancyService;
import com.park.api.web.dto.UserResponseDTO;
import com.park.api.web.dto.VacancyCreateDTO;
import com.park.api.web.dto.VacancyResponseDTO;
import com.park.api.web.dto.mapper.VacancyMapper;
import com.park.api.web.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/vacancy")
@Tag(name = "Vacancy", description = "Contains all operations related to the vacancy resource")
public class VacancyController {

    private final VacancyService vacancyService;

    @Operation(summary = "Create a new vacancy", description = "Request requires authentication.",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(responseCode = "201",
                    description = "resource created successfully",
                    headers = @Header(name = HttpHeaders.LOCATION, description = "URL of the created resource")),
                    @ApiResponse(responseCode = "403",
                            description = "Access not permitted to the Client profile",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "409",
                    description = "vacancy already registered!",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422",
                    description = "invalid input data",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
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

    @Operation(summary = "Retrieve a vacancy by code", description = "Request requires authentication. Access allowed only to ADMIN",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(responseCode = "200",
                    description = "Resource retrieved successfully!",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = VacancyResponseDTO.class))),
            @ApiResponse(responseCode = "403",
                    description = "Access not permitted to the Client profile",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404",
                    description = "Resource not found.",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VacancyResponseDTO> getByCode(@PathVariable String code){
        Vacancy vacancy = vacancyService.findByCode(code);
        return ResponseEntity.ok(VacancyMapper.toDTO(vacancy));
    }
}
