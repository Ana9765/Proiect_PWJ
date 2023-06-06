package com.project.wine_store.service;

import com.project.wine_store.model.Client;
import com.project.wine_store.model.WebOrder;
import com.project.wine_store.repository.WebOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebOrderService {

    private WebOrderRepository webOrderRepository;

    public WebOrderService(WebOrderRepository webOrderRepository) {
        this.webOrderRepository = webOrderRepository;
    }

    // Lista de comenzi ale unui client
    public List<WebOrder> getWebOrders(Client client) {
        return webOrderRepository.findByClient(client);
    }


}
