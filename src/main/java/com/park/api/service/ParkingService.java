package com.park.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ClientVacancyService clientVacancyService;
    private final ClientService clientService;
    private final VacancyService vacancyService;

    
}
