package com.project.wine_store.repository;

import com.project.wine_store.model.Client;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ClientRepository extends ListCrudRepository<Client, Long> {
    Optional<Client> findByUsernameIgnoreCase(String username);

    Optional<Client> findByEmailIgnoreCase(String email);


}
