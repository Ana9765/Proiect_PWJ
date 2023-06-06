package com.project.wine_store.api.controller;

import com.project.wine_store.model.Address;
import com.project.wine_store.model.Client;
import com.project.wine_store.model.WebOrder;
import com.project.wine_store.model.WebOrderQuantity;
import com.project.wine_store.repository.WebOrderQuantityRepository;
import com.project.wine_store.repository.WebOrderRepository;
import com.project.wine_store.repository.WineRepository;
import com.project.wine_store.service.WebOrderQuantityService;
import com.project.wine_store.service.WebOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/order")
public class WebOrderController {

    private WebOrderService webOrderService;
    private WineRepository wineRepository;
    private final WebOrderRepository webOrderRepository;
    private WebOrderQuantityRepository webOrderQuantityRepository;
    private WebOrderQuantityService webOrderQuantityService;

    public WebOrderController(WebOrderService webOrderService,
                              WineRepository wineRepository, WebOrderRepository webOrderRepository, WebOrderQuantityRepository webOrderQuantityRepository, WebOrderQuantityService webOrderQuantityService) {
        this.webOrderService = webOrderService;
        this.wineRepository = wineRepository;
        this.webOrderRepository = webOrderRepository;
        this.webOrderQuantityRepository = webOrderQuantityRepository;
        this.webOrderQuantityService = webOrderQuantityService;
    }

    // Afiseaza toate comenzile clientului logat
    @GetMapping
    public List<WebOrder> getWebOrders(@AuthenticationPrincipal Client client) {
        return webOrderService.getWebOrders(client);
    }


    // Afiseaza comenzile ce contin un anumit tip de vin
    @GetMapping("/specificWine")
    public List<WebOrder> getSpecificWineWebOrders(@AuthenticationPrincipal Client client, @RequestParam String wineName) {
        // lista tuturor comenzilor clientului
        List<WebOrder> webOrderOptional = webOrderService.getWebOrders(client);
        // id-ul vinului specific
        Long specificWineID = wineRepository.findByName(wineName).getId();
        // lista pentru comenzile ce contin vinul specific
        List<WebOrder> postedWebOrderList = new ArrayList<>();
        for (int i = 0; i<webOrderOptional.size();i++) {
            //luam cantitatile pentru fiecare comanda
            List<WebOrderQuantity> webOrderQuantityList = webOrderQuantityService.getWebOrderQuantities(webOrderOptional.get(i));
            for (int k = 0; k < webOrderQuantityList.size(); k++) {
                // daca gasim vinul specific
                if (Objects.equals(webOrderQuantityList.get(k).getWine().getName(), wineName)) {
                    // il adaugam intr-o lista
                    postedWebOrderList.add(webOrderOptional.get(i));
                }
            }
        }
        // afisam doar lista cu comenzile clientului ce contin vinul specific
        return postedWebOrderList;
    }

}
