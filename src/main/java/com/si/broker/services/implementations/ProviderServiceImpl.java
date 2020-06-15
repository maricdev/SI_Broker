package com.si.broker.services.implementations;


import com.si.broker.BrokerApplication;
import com.si.broker.api.v1.mappers.IProviderMapper;
import com.si.broker.api.v1.model.ProviderDTO;
import com.si.broker.model.Provider;
import com.si.broker.model.User;
import com.si.broker.repositories.IProviderRepository;
import com.si.broker.repositories.IUserRepository;
import com.si.broker.services.IProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProviderServiceImpl implements IProviderService {

    private final IProviderRepository providerRepository;
    private final IProviderMapper providerMapper;
    private final IUserRepository userRepository;

    @Autowired
    public ProviderServiceImpl(IProviderRepository providerRepository, IProviderMapper providerMapper, IUserRepository userRepository) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
        this.userRepository = userRepository;
    }

    public List<ProviderDTO> getAllProviders() {

        return providerRepository.findAll()
                .stream()
                .map(providerMapper::providerToProviderDTO)
                .collect(Collectors.toList());
    }

    public ProviderDTO getProviderById(Long id) {
        return providerMapper.providerToProviderDTO(providerRepository.getOne(id));
    }

    @Override
    public Boolean deleteProviderById(Long id) {
        providerRepository.deleteById(id);
        return true;
    }

    public ProviderDTO getProviderByEmail(String email) {
        return providerMapper.providerToProviderDTO(providerRepository.findByEmail(email));
    }

    public ProviderDTO getProviderByUsername(String username) {
        return providerMapper.providerToProviderDTO(providerRepository.findByUsername(username));
    }

    public ProviderDTO createProvider(ProviderDTO providerDTO) {

        List<User> users = userRepository.findAll();
        boolean exist = false;
        for (User u : users) {
            if (u.getEmail().equals(providerDTO.getEmail())) {
                exist = true;
                break;
            } else if (u.getUsername().equals(providerDTO.getUsername())) {
                exist = true;
                break;
            }
        }
        List<ProviderDTO> providers = getAllProviders();
        for (ProviderDTO p : providers) {
            if (p.getEmail().equals(providerDTO.getEmail())) {
                exist = true;
                break;
            } else if (p.getUsername().equals(providerDTO.getUsername())) {
                exist = true;
                break;
            }
        }
        if (exist == true)
            return null;

        providerDTO.setPassword(BrokerApplication.bCryptPasswordEncoder().encode(providerDTO.getPassword()));
        return saveAndReturnDTO(providerMapper.providerDtoToProvider(providerDTO));
    }

    public ProviderDTO updateProvider(long id, ProviderDTO providerDTO) {

        Provider inRepository = providerRepository.getOne(id);

        Provider providerToSave = providerMapper.providerDtoToProvider(providerDTO);
        if (inRepository == null)
            return null;

        if (providerToSave.getName() == null)
            providerToSave.setName(inRepository.getName());
        if (providerToSave.getHost() == null)
            providerToSave.setHost(inRepository.getHost());
        if (providerToSave.getEmail() == null)
            providerToSave.setEmail(inRepository.getEmail());
        if (providerToSave.getUsername() == null)
            providerToSave.setUsername(inRepository.getUsername());
        if (providerToSave.getPassword() == null)
            providerToSave.setPassword(inRepository.getPassword());
        else
            providerToSave.setPassword(BrokerApplication.bCryptPasswordEncoder().encode(providerDTO.getPassword()));
//
//        providerToSave.setDateCreated(inRepository.getDateCreated());
//        providerToSave.setDateModified(new Date());
        providerToSave.setId(id);

        return saveAndReturnDTO(providerToSave);
    }

    //Utils
    private ProviderDTO saveAndReturnDTO(Provider provider) {
        providerRepository.save(provider);
        ProviderDTO toReturn = providerMapper.providerToProviderDTO(provider);
        return toReturn;
    }
}
