package com.park.api.service;

import com.park.api.entities.ClientVacancy;
import com.park.api.exception.EntityNotFoundException;
import com.park.api.repositories.ClientVacancyRepository;
import com.park.api.repositories.projection.ClientVacancyProjection;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientVacancyService {

    private final ClientVacancyRepository repository;

    @Transactional
    public ClientVacancy save(ClientVacancy clientVacancy){
        return repository.save(clientVacancy);
    }

    @Transactional
    public ClientVacancy findByReceipt(String receipt) {
        return repository.findByReceiptAndExitDateIsNull(receipt).orElseThrow(
                ()-> new EntityNotFoundException(
                        String.format("Receipt not found or check-out already done.")
                )
        );
    }

    @Transactional
    public long getTotalTimesParkingComplete(String cpf) {
        return repository.countByClientCpfAndExitDateNotNull(cpf);
    }

    @Transactional
    public Page<ClientVacancyProjection> findAllByClientCpf(String cpf, Pageable pageable) {
        return repository.findAllByCloientCpf(cpf, pageable);
    }

    @Transactional
    public Page<ClientVacancyProjection> findAllByUserId(Long id, Pageable pageable) {
        return repository.findAllByClientUserId(id, pageable);
    }
}
