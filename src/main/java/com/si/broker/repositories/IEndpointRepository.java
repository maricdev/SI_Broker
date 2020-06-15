package com.si.broker.repositories;

import com.si.broker.model.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IEndpointRepository extends JpaRepository<Endpoint, Long> {
    List<Endpoint> findAll();
 //   List<EndpointDTO> findAllByRolesName(String name);
}
