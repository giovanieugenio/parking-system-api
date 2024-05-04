package com.park.api.service;

import com.park.api.entities.Client;
import com.park.api.exception.CpfUniqueViolationException;
import com.park.api.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public Client save(Client client) throws CpfUniqueViolationException {
        try{
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException e) {
            throw new CpfUniqueViolationException(String.format("Unable to register. CPF '%s' already exists in the system", client.getCpf()));
        }
    }
}
