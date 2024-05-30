package com.park.api.service;

import com.park.api.entities.Vacancy;
import com.park.api.exception.CodeUniqueViolationException;
import com.park.api.exception.EntityNotFoundException;
import com.park.api.repositories.VacancyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VacancyService {

    private final VacancyRepository vacancyRepository;

    @Transactional
    public Vacancy save(Vacancy vacancy){
        try{
            return vacancyRepository.save(vacancy);
        } catch (DataIntegrityViolationException e){
            throw new CodeUniqueViolationException(String.format("vacancy with code '%s' is already registered", vacancy.getCode()));
        }
    }

    @Transactional
    public Vacancy findByCode(String code){
        return vacancyRepository.findByCode(code).orElseThrow(
                ()-> new EntityNotFoundException(String.format("vacancy with code '%s' not found", code)));
    }
}
