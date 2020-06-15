package com.si.broker.services.implementations;

import com.si.broker.api.v1.mappers.IEndpointMapper;
import com.si.broker.api.v1.model.EndpointDTO;
import com.si.broker.model.Endpoint;
import com.si.broker.repositories.IEndpointRepository;
import com.si.broker.services.IEndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EndpointServiceImpl implements IEndpointService {

    private final IEndpointRepository endpointRepository;
    private final IEndpointMapper endpointMapper;

    @Autowired
    public EndpointServiceImpl(IEndpointRepository endpointRepository, IEndpointMapper endpointMapper) {
        this.endpointRepository = endpointRepository;
        this.endpointMapper = endpointMapper;
    }


    @Override
    public EndpointDTO getEndpointById(Long id) {
        return endpointMapper.endpointToEndpointDto(endpointRepository.getOne(id));
    }

    @Override
    public List<EndpointDTO> getAllEndpoints() {
        return endpointRepository.findAll()
                .stream()
                .map(endpointMapper::endpointToEndpointDto)
                .collect(Collectors.toList());
    }

    @Override
    public EndpointDTO createEndpoint(EndpointDTO endpointDTO) {
        return saveAndReturnDTO(endpointMapper.endpointDtoToEndpoint(endpointDTO));
    }

    @Override
    public Boolean deleteEndpointById(Long id) {
        endpointRepository.deleteById(id);
        return true;
    }

    @Override
    public EndpointDTO updateEndpoint(Long id, EndpointDTO endpointDTO) {
        Endpoint inRepository = endpointRepository.getOne(id);

        Endpoint endpointToSave = endpointMapper.endpointDtoToEndpoint(endpointDTO);
        if (inRepository == null)
            return null;

        if (endpointToSave.getName() == null)
            endpointToSave.setName(inRepository.getName());

        if (endpointToSave.getType() == null)
            endpointToSave.setType(inRepository.getType());

        if (endpointToSave.getRoute() == null)
            endpointToSave.setRoute(inRepository.getRoute());

        if (endpointToSave.getDescription() == null)
            endpointToSave.setDescription(inRepository.getDescription());

        if (endpointToSave.getMethod() == null)
            endpointToSave.setMethod(inRepository.getMethod());

        if (endpointToSave.getServices_id() == null)
            endpointToSave.setServices_id(inRepository.getServices_id());
//
//        if (endpointToSave.getDateCreated() == null)
//            endpointToSave.setDateCreated(inRepository.getDateCreated());


        endpointToSave.setId(id);

        return saveAndReturnDTO(endpointToSave);
    }


    private EndpointDTO saveAndReturnDTO(Endpoint endpoint) {
        endpointRepository.save(endpoint);
        EndpointDTO toReturn = endpointMapper.endpointToEndpointDto(endpoint);
        return toReturn;
    }
}
