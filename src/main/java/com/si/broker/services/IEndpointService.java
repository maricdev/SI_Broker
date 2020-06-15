package com.si.broker.services;

import com.si.broker.api.v1.model.EndpointDTO;

import java.util.List;

public interface IEndpointService {

    EndpointDTO getEndpointById(Long id);

    List<EndpointDTO> getAllEndpoints();

    EndpointDTO createEndpoint(EndpointDTO serviceDTO);

    Boolean deleteEndpointById(Long id);

    EndpointDTO updateEndpoint(Long id, EndpointDTO serviceDTO);

}
