package com.project.wine_store.service;

import com.project.wine_store.model.Wine;
import com.project.wine_store.repository.WineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WineService {

    private WineRepository wineRepository;

    public WineService(WineRepository wineRepository) {
        this.wineRepository = wineRepository;
    }

    // Lista de vinuri
    public List<Wine> getWine() {
        return wineRepository.findAll();
    }

    // Gasim un vin dupa nume
    public Wine getSpecificWine(String specificName) {return wineRepository.findByName(specificName);}

}
