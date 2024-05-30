package com.park.api.web.controller;

import com.park.api.entities.Client;
import com.park.api.exception.CpfUniqueViolationException;
import com.park.api.jwt.JwtUserDetails;
import com.park.api.repositories.projection.ClientProjection;
import com.park.api.service.ClientService;
import com.park.api.service.UserService;
import com.park.api.web.dto.ClientCreateDTO;
import com.park.api.web.dto.ClientResponseDTO;
import com.park.api.web.dto.PageableDTO;
import com.park.api.web.dto.mapper.ClientMapper;
import com.park.api.web.dto.mapper.PageableMapper;
import com.park.api.web.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Client", description = "Contains all operations related to client resources")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a new client", description = "Restricted access to CLIENT profile",
            security = @SecurityRequirement(name = "security"),
            responses = {
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

      @Operation(summary = "Find a client", description = "Resource to find a client by ID",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(responseCode = "200",
                    description = "resource found successfully",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ClientResponseDTO.class))),
            @ApiResponse(responseCode = "409",
                    description = "Error: Client not found!",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403",
                    description = "Resource not allowed for CLIENT profile",
                    content = @Content(mediaType = "application/json;charset=UTF-8",
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDTO> getById(@PathVariable Long id){
        Client client = clientService.getById(id);
        return ResponseEntity.ok().body(ClientMapper.toDTO(client));
    }

    @Operation(summary = "Find a list client", description = "Resource to find a client by ID",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                        content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "total page elements"
                    ),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                            description = "location of one element per page"
                    ),
                    @Parameter(in = QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id,asc")),
                            description = "sort the results. ")
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "resource found successfully",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ClientResponseDTO.class))),
                    @ApiResponse(responseCode = "403",
                            description = "Resource not allowed for CLIENT profile",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDTO> getAll(@Parameter(hidden = true)@PageableDefault(size = 5, sort = {"name"}) Pageable pageable){
        Page<ClientProjection> client = clientService.getAllClient(pageable);
        return ResponseEntity.ok(PageableMapper.toDTO(client));
    }

    @Operation(summary = "Find a list client", description = "Resource to find a client by ID",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "resource found successfully",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ClientResponseDTO.class))),
                    @ApiResponse(responseCode = "403",
                            description = "Resource not allowed for CLIENT profile",
                            content = @Content(mediaType = "application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/details")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDTO> getDetails(@AuthenticationPrincipal JwtUserDetails userDetails){
        Client client = clientService.getByUserId(userDetails.getId());
        return ResponseEntity.ok(ClientMapper.toDTO(client));
    }
}
