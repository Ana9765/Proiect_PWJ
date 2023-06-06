package com.project.wine_store.repository;

import com.project.wine_store.model.Address;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface AddressRepository extends ListCrudRepository<Address, Long> {
    List<Address> findByClient_Id(Long id);

    @Override
    void deleteById(Long aLong);
}
