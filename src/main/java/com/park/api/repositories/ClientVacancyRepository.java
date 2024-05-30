package com.park.api.repositories;

import com.park.api.entities.ClientVacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientVacancyRepository extends JpaRepository<ClientVacancy, Long> {
}
