package com.si.broker.repositories;

import com.si.broker.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProviderRepository extends JpaRepository<Provider, Long> {

    List<Provider> findAll();

    Provider findByEmail(String email);

    Provider findByUsername(String username);
}
