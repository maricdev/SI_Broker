package com.si.broker.services;


import com.si.broker.api.v1.model.ProviderDTO;

import java.util.List;

public interface IProviderService {

    //GET
    List<ProviderDTO> getAllProviders();

    ProviderDTO getProviderById(Long id);

    ProviderDTO getProviderByEmail(String email);

    ProviderDTO getProviderByUsername(String username);

    Boolean deleteProviderById(Long id);


    //PUT
    ProviderDTO createProvider(ProviderDTO providerDTO);

    ProviderDTO updateProvider(long id, ProviderDTO providerDTO);

}