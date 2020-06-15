package com.si.broker.api.v1.mappers;

import com.si.broker.api.v1.model.ParameterDTO;
import com.si.broker.model.Parameter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IParameterMapper {

    IParameterMapper instance = Mappers.getMapper(IParameterMapper.class);

    Parameter parametarDTOToParametar(ParameterDTO parameterDTO);

    ParameterDTO parametarToParametarDTO(Parameter parameter);

}
