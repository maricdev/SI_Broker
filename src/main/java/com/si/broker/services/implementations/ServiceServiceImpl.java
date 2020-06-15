package com.si.broker.services.implementations;

import com.si.broker.api.v1.mappers.IServiceMapper;
import com.si.broker.api.v1.model.ServiceDTO;
import com.si.broker.api.v1.model.UserDTO;
import com.si.broker.model.Endpoint;
import com.si.broker.model.Endpoint_Role;
import com.si.broker.model.Service;
import com.si.broker.model.Token;
import com.si.broker.repositories.IServiceRepository;
import com.si.broker.services.IProviderService;
import com.si.broker.services.IServiceService;
import com.si.broker.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements IServiceService {

    private final IServiceRepository serviceRepository;
    private final IServiceMapper serviceMapper;
    private final IProviderService providerService;
    private final IUserService userService;

    @Autowired
    public ServiceServiceImpl(IServiceRepository serviceRepository, IServiceMapper serviceMapper, IProviderService providerService, IUserService userService) {
        this.serviceRepository = serviceRepository;
        this.serviceMapper = serviceMapper;
        this.providerService = providerService;
        this.userService = userService;
    }

    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll()
                .stream()
                .map(serviceMapper::serviceToServiceDTO)
                .collect(Collectors.toList());
    }

    public List<ServiceDTO> getAllServicesByPrivilege(Token token) {
        List<Service> services = serviceRepository.findAllByProviderId(token.getProvider_id());
        if (token.getIsProvider() == false) {
            for (Service service : services) {
                Boolean exist = false;
                for (Endpoint endpoint : service.getEndpoints()) {
                    for (Endpoint_Role role : endpoint.getPermissions()) {
                        if (token.getIsAdmin() && role.getRole().getName().equals("ADMIN")) {
                            exist = true;
                        } else if (token.getIsSaradnik() && role.getRole().getName().equals("SARADNIK")) {
                            exist = true;
                        } else if (token.getIsKorisnik() && role.getRole().getName().equals("KORISNIK")) {
                            exist = true;
                        }
                        if (exist == true)
                            break;
                    }
                    if (exist == false)
                        service.getEndpoints().remove(endpoint);
                }
            }
        }
        return services.stream().map(serviceMapper::serviceToServiceDTO).collect(Collectors.toList());
    }

    public ServiceDTO getServiceById(Long id) {
        return serviceMapper.serviceToServiceDTO(serviceRepository.getOne(id));
    }

    public ServiceDTO getServiceByIdByPrivilege(Long id, Token token) {
        UserDTO user = userService.getUserDtoById(id);
        if (user.getProviders_id() == token.getProvider_id())
            return getServiceById(id);
        else
            return null;
    }

    @Override
    public ServiceDTO createService(ServiceDTO serviceDTO) {

        return saveAndReturnDTO(serviceMapper.serviceDtoToService(serviceDTO));
    }

    @Override
    public Boolean deleteServiceById(Long id, Token token) {
        ServiceDTO service = getServiceById(id);
        Boolean deleted = null;
        if ((token.getIsAdmin() && token.getProvider_id() == service.getProviders_id()) ||
                (token.getIsProvider() && token.getProvider_id() == token.getId())) {
            serviceRepository.deleteById(id);
            deleted = true;
        } else
            deleted = false;
        return deleted;
    }

    @Override
    public ServiceDTO updateService(Long id, ServiceDTO serviceDTO, Token token) {
        ServiceDTO service = getServiceById(id);

        if ((token.getIsAdmin() && token.getProvider_id() == service.getProviders_id()) ||
                (token.getIsProvider() && token.getProvider_id() == token.getId())) {

            Service inRepository = serviceRepository.getOne(id);

            Service serviceToSave = serviceMapper.serviceDtoToService(serviceDTO);
            if (inRepository == null)
                return null;

            if (serviceToSave.getName() == null)
                serviceToSave.setName(inRepository.getName());
            if (serviceToSave.getRoute() == null)
                serviceToSave.setRoute(inRepository.getRoute());
            if (serviceToSave.getComposite() == null)
                serviceToSave.setComposite(inRepository.getComposite());
            if (serviceToSave.getDescription() == null)
                serviceToSave.setDescription(inRepository.getDescription());
            if (serviceToSave.getProviders_id() == null)
                serviceToSave.setProviders_id(inRepository.getProviders_id());

            serviceToSave.setId(id);
            return saveAndReturnDTO(serviceToSave);
        }
        return null;
    }

    private ServiceDTO saveAndReturnDTO(Service service) {

        serviceRepository.save(service);
        ServiceDTO toReturn = serviceMapper.serviceToServiceDTO(service);
        return toReturn;

    }
}
