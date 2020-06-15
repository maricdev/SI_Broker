package com.si.broker.api.v1.mappers;

import com.si.broker.api.v1.model.ProviderDTO;
import com.si.broker.model.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IProviderMapper {

    IProviderMapper instance = Mappers.getMapper(IProviderMapper.class);

    ProviderDTO providerToProviderDTO(Provider provider);

    //    @Mapping(source = "name", target = "firstName")
    Provider providerDtoToProvider(ProviderDTO providerDTO);

}
