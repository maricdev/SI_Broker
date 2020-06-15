package com.si.broker.services.implementations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.si.broker.api.v1.mappers.IProviderMapper;
import com.si.broker.api.v1.mappers.IServiceMapper;
import com.si.broker.api.v1.model.EndpointDTO;
import com.si.broker.api.v1.model.ProviderDTO;
import com.si.broker.model.*;
import com.si.broker.orkestracija.Generator;
import com.si.broker.repositories.ILoggerRepository;
import com.si.broker.repositories.IPrivilegesRepository;
import com.si.broker.services.*;
import com.si.broker.utils.Constants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutingServiceImpl implements IRoutingService {

    private final IPrivilegesRepository privilegiesRepository;
    private final IUserService userService;
    private final IServiceService serviceService;
    private final IServiceMapper serviceMapper;
    private final IProviderService providerService;
    private final IProviderMapper providerMapper;
    private final IEndpointService endpointService;
    private final ILoggerRepository loggerRepository;

    public RoutingServiceImpl(IPrivilegesRepository privilegiesRepository, IUserService userService, IServiceService serviceService, IServiceMapper serviceMapper, IProviderService providerService, IProviderMapper providerMapper, IEndpointService endpointService, ILoggerRepository loggerRepository) {

        this.privilegiesRepository = privilegiesRepository;
        this.userService = userService;
        this.serviceService = serviceService;
        this.serviceMapper = serviceMapper;
        this.providerService = providerService;
        this.providerMapper = providerMapper;
        this.endpointService = endpointService;
        this.loggerRepository = loggerRepository;
    }

    public User getUserByToken(Token token) {
        return userService.getUserByUsername(token.getUsername());
    }

    public Provider getProviderByToken(Token token) {
        return providerMapper.providerDtoToProvider(providerService.getProviderByUsername(token.getUsername()));
    }

    public boolean checkIfUserCanAccess(Token token, Long endpoint_id) {
        EndpointDTO endpoint = endpointService.getEndpointById(endpoint_id);
        if (token.getIsProvider() && token.getId() == token.getProvider_id()) // ako je provajder proverava se da li je isti id
            return true;
        else if (endpoint.getService().getProviders_id() == token.getProvider_id()) { // ako je korisnik
            for (Endpoint_Role role : endpoint.getPermissions()) {
                if (token.getIsAdmin() && role.getRole().getName().equals("ADMIN")) {
                    return true;
                } else if (token.getIsSaradnik() && role.getRole().getName().equals("SARADNIK")) {
                    return true;
                } else if (token.getIsKorisnik() && role.getRole().getName().equals("KORISNIK")) {
                    return true;
                }
            }
            return false;
        } else
            return false;
    }

    public String getFieldsByRole(Token token, Long endpoint_id) {
        EndpointDTO endpoint = endpointService.getEndpointById(endpoint_id);

        if (token.getIsProvider() && token.getId() == token.getProvider_id()) // ako je provajder proverava se da li je isti id
            return null;
        else if (endpoint.getService().getProviders_id() == token.getProvider_id()) { // ako je korisnik
            for (Endpoint_Role role : endpoint.getPermissions()) {
                if (token.getIsAdmin() && role.getRole().getName().equals("ADMIN")) {
                    return role.getExclude();
                } else if (token.getIsSaradnik() && role.getRole().getName().equals("SARADNIK")) {
                    return role.getExclude();
                } else if (token.getIsKorisnik() && role.getRole().getName().equals("KORISNIK")) {
                    return role.getExclude();
                }
            }
            return null;
        } else
            return null;
    }

    public String createJsonFilter(String fields, String body) {
        JSONObject json = new JSONObject();
        JSONObject dataObject = null;
        JSONArray dataArray = null;

        if (body.startsWith("{"))
            dataObject = new JSONObject(body);
        else if (body.startsWith("["))
            dataArray = new JSONArray(body);

        if (dataObject != null || dataArray != null) {
            if (fields == null || fields.trim().equals(""))
                json.put("fields", JSONObject.NULL);
            else
                json.put("fields", fields);
            if (dataObject != null)
                json.put("data", dataObject);
            else if (dataArray != null)
                json.put("data", dataArray);
            System.out.println(json.toString());
            return json.toString();
        }
        return null;
    }

    @Override
    public boolean checkIfUserCanAccessToCompositeService(Token token, Long service_id) {
        com.si.broker.model.Service service = serviceMapper.serviceDtoToService(serviceService.getServiceById(service_id));
        List<Endpoint> endpoints = service.getComposite_services();
        for (Endpoint e : endpoints) {
            Boolean allow = false;
            for (Endpoint_Role r : e.getPermissions()) {
                if (token.getIsProvider() && e.getService().getProviders_id() == token.getProvider_id() && r.getRole().getName().equals("PROVIDER"))
                    allow = true;
                else if (token.getIsAdmin() && e.getService().getProviders_id() == token.getProvider_id() && r.getRole().getName().equals("ADMIN"))
                    allow = true;
                else if (token.getIsSaradnik() && e.getService().getProviders_id() == token.getProvider_id() && r.getRole().getName().equals("SARADNIK"))
                    allow = true;
                else if (token.getIsKorisnik() && e.getService().getProviders_id() == token.getProvider_id() && r.getRole().getName().equals("KORISNIK"))
                    allow = true;
            }
            if (allow == false)
                return false;
        }
        return true;
    }

    @Override
    public boolean checkEndpointService(Long service_id, Long endpoint_id) {
        com.si.broker.model.Service service = serviceMapper.serviceDtoToService(serviceService.getServiceById(service_id));
        for (Endpoint e : service.getEndpoints()) {
            if (e.getId() == endpoint_id)
                return true;
        }
        return false;
    }

    public boolean checkIfServiceIsComposite(Long service_id) {
        com.si.broker.model.Service service = serviceMapper.serviceDtoToService(serviceService.getServiceById(service_id));

        return service.getComposite();
    }

    public List<Endpoint> getEndpointsPlan(Long service_id) {
        com.si.broker.model.Service service = serviceMapper.serviceDtoToService(serviceService.getServiceById(service_id));
        Generator generator = new Generator(endpointService);
        return generator.generatePlans(service.getComposite_services());
    }

    public String getFullPath(Long endpoint_id) {
        EndpointDTO endpoint = endpointService.getEndpointById(endpoint_id);
        ProviderDTO provider = providerService.getProviderById(endpoint.getService().getProviders_id());

        return provider.getHost() + ":" + provider.getPort() + "/" + endpoint.getService().getRoute() + "/" + endpoint.getRoute();
    }


    public Token getTokenInfoByToken(String jsonToken) {

        Token token = new Token();

        DecodedJWT jwt = JWT.decode(JWT.require(Algorithm.HMAC512(Constants.SECRET_KEY.getBytes()))
                .build()
                .verify(jsonToken.replace(Constants.TOKEN_PREFIX, ""))
                .getToken());

        token.setId(jwt.getClaim("ID").asLong());

        token.setUsername(jwt.getSubject());

        token.setEmail(jwt.getClaim("EMAIL").asString());

        token.setIsAdmin(jwt.getClaim("ADMIN").asBoolean());

        token.setIsProvider(jwt.getClaim("PROVIDER").asBoolean());

        token.setIsKorisnik(jwt.getClaim("KORISNIK").asBoolean());

        token.setIsSaradnik(jwt.getClaim("SARADNIK").asBoolean());

        token.setProvider_id(jwt.getClaim("PROVIDER_ID").asLong());

        token.setExpiration(jwt.getClaim("exp").asDate());

        return token;
    }


    public boolean createLogger(Boolean isSuccess, Boolean isBroker, String services_path, String endpoints_path, Long services_id, Long endpoints_id, Long providers_id, Long users_id) {
        Logger logger = new Logger();
        logger.setIsSuccess(isSuccess);
        logger.setIsBroker(isBroker);
        logger.setServices_path(services_path);
        logger.setEndpoints_path(endpoints_path);
        logger.setServices_id(services_id);
        logger.setEndpoints_id(endpoints_id);
        logger.setProviders_id(providers_id);
        logger.setUsers_id(users_id);
        loggerRepository.save(logger);
        return true;
    }


}
