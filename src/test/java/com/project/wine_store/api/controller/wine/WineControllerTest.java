package com.project.wine_store.api.controller.wine;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WineControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void testWineList() throws Exception {
        // pentru un request de tip get pe lista de vinuri, ne asteptam ca statusul sa fie OK
        mvc.perform(get("/wine")).andExpect(status().is(HttpStatus.OK.value()));
    }
}
