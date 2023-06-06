package com.project.wine_store.repository;

import com.project.wine_store.model.Client;
import com.project.wine_store.model.EmailVerificationToken;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends ListCrudRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);

    // cautam ultimul verification token asignat clientului
    List<EmailVerificationToken> findByClient_IdOrderByIdDesc(Long id);
}
