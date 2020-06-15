package com.si.broker.api.v1.mappers;

import com.si.broker.api.v1.model.ServiceDTO;
import com.si.broker.model.Service;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IServiceMapper {

    IServiceMapper instance = Mappers.getMapper(IServiceMapper.class);

    ServiceDTO serviceToServiceDTO(Service service);

    Service serviceDtoToService(ServiceDTO serviceDTO);

    List<ServiceDTO> serviceListToServiceListDTO(List<Service> service);

    List<Service> serviceListDtoToServiceList(List<ServiceDTO> serviceDTO);
}
