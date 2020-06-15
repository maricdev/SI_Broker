package com.si.broker.repositories;

import com.si.broker.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findAll();

    List<Service> findAllByProviderId(Long id);

}
