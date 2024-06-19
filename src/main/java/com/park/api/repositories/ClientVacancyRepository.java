package com.park.api.repositories;

import com.park.api.entities.ClientVacancy;
import com.park.api.repositories.projection.ClientVacancyProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientVacancyRepository extends JpaRepository<ClientVacancy, Long> {
    Optional<ClientVacancy> findByReceiptAndExitDateIsNull(String receipt);

    long countByClientCpfAndExitDateNotNull(String cpf);

    Page<ClientVacancyProjection> findAllByCloientCpf(String cpf, Pageable pageable);

    Page<ClientVacancyProjection> findAllByClientUserId(Long id, Pageable pageable);
}
