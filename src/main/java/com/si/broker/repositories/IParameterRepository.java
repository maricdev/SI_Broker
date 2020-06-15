package com.si.broker.repositories;

import com.si.broker.model.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IParameterRepository extends JpaRepository<Parameter, Long> {

    List<Parameter> findAll();
}
