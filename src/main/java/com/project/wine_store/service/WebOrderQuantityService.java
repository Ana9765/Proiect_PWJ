package com.project.wine_store.service;

import com.project.wine_store.model.Client;
import com.project.wine_store.model.WebOrder;
import com.project.wine_store.model.WebOrderQuantity;
import com.project.wine_store.repository.WebOrderQuantityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class WebOrderQuantityService {

    private WebOrderQuantityRepository webOrderQuantityRepository;

    public WebOrderQuantityService(WebOrderQuantityRepository webOrderQuantityRepository) {
        this.webOrderQuantityRepository = webOrderQuantityRepository;
    }

    public List<WebOrderQuantity> getWebOrderQuantities(WebOrder webOrder) {
        return webOrderQuantityRepository.findByWebOrder_Id(webOrder.getId());
    }

}
