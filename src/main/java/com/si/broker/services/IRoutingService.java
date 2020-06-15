package com.si.broker.services;


import com.si.broker.model.Endpoint;
import com.si.broker.model.Provider;
import com.si.broker.model.Token;
import com.si.broker.model.User;

import java.util.List;

public interface IRoutingService {

    User getUserByToken(Token token);

    Provider getProviderByToken(Token token);

    Token getTokenInfoByToken(String token);

    boolean checkIfUserCanAccess(Token token, Long endpoint);

    String getFieldsByRole(Token token, Long endpoint);

    String createJsonFilter(String fields, String body);

    boolean checkIfUserCanAccessToCompositeService(Token token, Long service);

    boolean checkEndpointService(Long service_id, Long endpoint_id);

    boolean checkIfServiceIsComposite(Long service_id);

    List<Endpoint> getEndpointsPlan(Long service_id);

    String getFullPath(Long endpoint_id);

    boolean createLogger(Boolean isSuccess, Boolean isBroker, String services_path, String endpoints_path, Long services_id, Long endpoints_id, Long providers_id, Long users_id);
}
