package com.park.api.web.dto.mapper;

import com.park.api.entities.Client;
import com.park.api.web.dto.ClientCreateDTO;
import com.park.api.web.dto.ClientResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {

    public static Client toClient(ClientCreateDTO clientDTO){
        return new ModelMapper().map(clientDTO, Client.class);
    }

    public static ClientResponseDTO toDTO(Client client){
        return new ModelMapper().map(client, ClientResponseDTO.class);
    }
}
