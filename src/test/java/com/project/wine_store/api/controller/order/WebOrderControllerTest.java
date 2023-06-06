package com.project.wine_store.api.controller.order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.wine_store.model.WebOrder;
import com.project.wine_store.repository.ClientRepository;
import com.project.wine_store.repository.WebOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WebOrderControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private WebOrderRepository webOrderRepository;

    // comenzile afisate trebuie sa fie comenzile pentru clientul logat - clientulA
    @Test
    @WithUserDetails("ClientA")
    public void testOrderListLoggedClientA() throws Exception{
        mvc.perform(get("/order")).andExpect(status().is(HttpStatus.OK.value())).andExpect(result -> {
            // transformam obiectul json in obiect java si primim o lista de comenzi
            String json = result.getResponse().getContentAsString();
            List<WebOrder> clientOrders = new ObjectMapper().readValue(json, new TypeReference<List<WebOrder>>() {});
            for (WebOrder order : clientOrders) {
                Assertions.assertEquals("ClientA", order.getClient().getUsername(), "Comenzile afisate ar trebui sa fie doar comenzile clientului logat");
            }
        });
    }

    // daca nu suntem logati in cont, nu primim lista de comenzi
    @Test
    public void testOrderListForNoLogin() throws Exception{
        mvc.perform(get("/order")).andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }
}
