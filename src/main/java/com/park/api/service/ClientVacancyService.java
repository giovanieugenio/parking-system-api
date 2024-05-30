package com.park.api.service;

import com.park.api.entities.ClientVacancy;
import com.park.api.repositories.ClientVacancyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientVacancyService {

    private final ClientVacancyRepository repository;

    @Transactional
    public ClientVacancy save(ClientVacancy clientVacancy){
        return repository.save(clientVacancy);
    }
}
