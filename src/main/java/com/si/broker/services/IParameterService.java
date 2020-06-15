package com.si.broker.services;

import com.si.broker.api.v1.model.ParameterDTO;

import java.util.List;

public interface IParameterService {

    List<ParameterDTO> getAllParametars();

    ParameterDTO getParametarById(Long id);

    ParameterDTO createParametar(ParameterDTO parameterDTO);

    Boolean deleteParametarById(Long id);

    ParameterDTO updateParametar(Long id, ParameterDTO parameterDTO);


}
