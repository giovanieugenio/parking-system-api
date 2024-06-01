package com.park.api.repositories;

import com.park.api.entities.ClientVacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientVacancyRepository extends JpaRepository<ClientVacancy, Long> {
    Optional<ClientVacancy> findByReceiptAndExitDateIsNull(String receipt);
}
