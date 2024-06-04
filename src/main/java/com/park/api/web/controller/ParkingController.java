package com.park.api.web.controller;

import com.park.api.entities.ClientVacancy;
import com.park.api.service.ClientVacancyService;
import com.park.api.service.ParkingService;
import com.park.api.web.dto.ParkingCreateDTO;
import com.park.api.web.dto.ParkingResponseDTO;
import com.park.api.web.dto.UserResponseDTO;
import com.park.api.web.dto.mapper.ClientVacancyMapper;
import com.park.api.web.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;
    private final ClientVacancyService clientVacancyService;

    @Operation(summary = "Check-In operation",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(responseCode = "201",
                    description = "resource created successfully",
                    headers = @Header(name = HttpHeaders.LOCATION, description = "URL of the created resource"),
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ParkingResponseDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Resource not found. Possible causes: <br/>" +
                            "CPF's client not registered in the system.<br/>" +
                            "No free cavancy.",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422",
                    description = "invalid input data",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403",
                    description = "User is not allowed to access this resource.",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDTO> checkIn(@RequestBody @Valid ParkingCreateDTO createDTO){
        ClientVacancy clientVacancy = ClientVacancyMapper.toClientVacancy(createDTO);
        parkingService.checkIn(clientVacancy);
        ParkingResponseDTO responseDTO = ClientVacancyMapper.toDTO(clientVacancy);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{receipt}")
                .buildAndExpand(clientVacancy.getReceipt())
                .toUri();
        return ResponseEntity.created(location).body(responseDTO);
    }

    @GetMapping("/check-in/{receipt}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ParkingResponseDTO> getByReceipt(@PathVariable String receipt){
        ClientVacancy clientVacancy = clientVacancyService.findByReceipt(receipt);
        ParkingResponseDTO responseDTO = ClientVacancyMapper.toDTO(clientVacancy);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/check-out/{receipt}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDTO> checkOut(@PathVariable String receipt){
        ClientVacancy clientVacancy = parkingService.checkOut(receipt);
        ParkingResponseDTO responseDTO = ClientVacancyMapper.toDTO(clientVacancy);
        return ResponseEntity.ok(responseDTO);
    }
}
