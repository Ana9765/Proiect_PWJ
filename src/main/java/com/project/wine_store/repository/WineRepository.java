package com.project.wine_store.repository;

import com.project.wine_store.model.Wine;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WineRepository extends ListCrudRepository<Wine, Long> {
    Wine findByName(String name);
}
