package com.project.wine_store.repository;

import com.project.wine_store.model.Client;
import com.project.wine_store.model.WebOrder;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebOrderRepository extends ListCrudRepository<WebOrder, Long> {
    // cautam comenzile dupa client
    List<WebOrder> findByClient(Client client);

}
