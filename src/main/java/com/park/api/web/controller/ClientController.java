package com.park.api.web.controller;

import com.park.api.entities.Client;
import com.park.api.exception.CpfUniqueViolationException;
import com.park.api.jwt.JwtUserDetails;
import com.park.api.service.ClientService;
import com.park.api.service.UserService;
import com.park.api.web.dto.ClientCreateDTO;
import com.park.api.web.dto.ClientResponseDTO;
import com.park.api.web.dto.UserResponseDTO;
import com.park.api.web.dto.mapper.ClientMapper;
import com.park.api.web.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Client", description = "Contains all operations related to client resources")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a new client", description = "Restricted access to CLIENT profile", responses = {
            @ApiResponse(responseCode = "201",
                    description = "resource created successfully",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ClientResponseDTO.class))),
            @ApiResponse(responseCode = "409",
                    description = "Error: CPF already exists!",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422",
                    description = "invalid input data",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403",
                    description = "Resource not allowed for ADMIN profile",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDTO> create(@RequestBody @Valid ClientCreateDTO clientDTO,
                                                    @AuthenticationPrincipal JwtUserDetails userDetails) throws CpfUniqueViolationException {
        Client client = ClientMapper.toClient(clientDTO);
        client.setUser(userService.findById(userDetails.getId()));
        clientService.save(client);
        return ResponseEntity.status(201).body(ClientMapper.toDTO(client));
    }

}
