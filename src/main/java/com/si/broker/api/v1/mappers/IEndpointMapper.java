package com.si.broker.api.v1.mappers;

import com.si.broker.api.v1.model.EndpointDTO;
import com.si.broker.model.Endpoint;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IEndpointMapper {

    IEndpointMapper instance = Mappers.getMapper(IEndpointMapper.class);

    EndpointDTO endpointToEndpointDto(Endpoint endpoint);


    Endpoint endpointDtoToEndpoint(EndpointDTO endpointDTO);
}
