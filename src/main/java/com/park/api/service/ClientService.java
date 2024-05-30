package com.park.api.service;

import com.park.api.entities.Client;
import com.park.api.exception.CpfUniqueViolationException;
import com.park.api.exception.EntityNotFoundException;
import com.park.api.repositories.ClientRepository;
import com.park.api.repositories.projection.ClientProjection;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public Client getById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Client '%s' not found", id)));
    }

    @Transactional
    public Page<ClientProjection> getAllClient(Pageable pageable) {
        return clientRepository.findAllPageable(pageable);
    }

    @Transactional
    public Client getByUserId(Long id) {
        return clientRepository.findByUserId(id);
    }
}
