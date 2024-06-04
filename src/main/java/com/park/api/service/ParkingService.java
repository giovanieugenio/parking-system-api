package com.park.api.service;

import com.park.api.entities.Client;
import com.park.api.entities.ClientVacancy;
import com.park.api.entities.Vacancy;
import com.park.api.utils.ParkingUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ClientVacancyService clientVacancyService;
    private final ClientService clientService;
    private final VacancyService vacancyService;

    @Transactional
    public ClientVacancy checkIn(ClientVacancy clientVacancy){
        Client client = clientService.findByCpf(clientVacancy.getClient().getCpf());
        clientVacancy.setClient(client);
        Vacancy vacancy = vacancyService.findByFreeVacancy();
        vacancy.setStatusVacancy(Vacancy.StatusVacancy.OCCUPIED);
        clientVacancy.setVacancy(vacancy);
        clientVacancy.setEntryDate(LocalDateTime.now());
        clientVacancy.setReceipt(ParkingUtils.generateReceipt());
        return clientVacancyService.save(clientVacancy);
    }

    @Transactional
    public ClientVacancy checkOut(String receipt) {
        ClientVacancy clientVacancy = clientVacancyService.findByReceipt(receipt);
        LocalDateTime exitDate = LocalDateTime.now();
        BigDecimal price = ParkingUtils.calculatePrice(clientVacancy.getEntryDate(), exitDate);
        clientVacancy.setPrice(price);
        long totalTimes = clientVacancyService.getTotalTimesParkingComplete(clientVacancy.getClient().getCpf());
        BigDecimal discount = ParkingUtils.calculateDiscount(price, totalTimes);
        clientVacancy.setDiscount(discount);
        clientVacancy.setExitDate(exitDate);
        clientVacancy.getVacancy().setStatusVacancy(Vacancy.StatusVacancy.FREE);
        return clientVacancyService.save(clientVacancy);
    }
}
