package com.si.broker.services;

import com.si.broker.api.v1.model.ServiceDTO;
import com.si.broker.model.Token;

import java.util.List;

public interface IServiceService {

    //GET
    List<ServiceDTO> getAllServices();

    List<ServiceDTO> getAllServicesByPrivilege(Token token);

    ServiceDTO getServiceById(Long id);

    ServiceDTO getServiceByIdByPrivilege(Long id, Token token);

    //POST
    ServiceDTO createService(ServiceDTO serviceDTO);

    ServiceDTO updateService(Long id, ServiceDTO serviceDTO, Token token);

    Boolean deleteServiceById(Long id, Token token);


}
