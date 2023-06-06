package com.project.wine_store.api.controller;

import com.project.wine_store.model.Wine;
import com.project.wine_store.service.WineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wine")

public class WineController {
    private WineService wineService;

    public WineController(WineService wineService) {
        this.wineService = wineService;
    }

    @GetMapping
    public List<Wine> getWine() {
        return wineService.getWine();
    }
}
