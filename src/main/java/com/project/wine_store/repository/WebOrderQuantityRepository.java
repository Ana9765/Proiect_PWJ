package com.project.wine_store.repository;

import com.project.wine_store.model.WebOrderQuantity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebOrderQuantityRepository extends ListCrudRepository<WebOrderQuantity, Long> {
    List<WebOrderQuantity> findByWebOrder_Id(Long id);

}
