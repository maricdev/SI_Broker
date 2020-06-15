package com.si.broker.controllers.v1;

import com.si.broker.api.v1.model.ProviderDTO;
import com.si.broker.model.Token;
import com.si.broker.services.IProviderService;
import com.si.broker.services.IRoutingService;
import com.si.broker.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(Constants.PROVIDERS_BASE_URL)
// api/v1/providers
public class ProviderController {

    private final IProviderService providerService;
    private final IRoutingService routingService;
    private Token token;

    @Autowired
    public ProviderController(IProviderService providerService, IRoutingService routingService) {
        this.providerService = providerService;
        this.routingService = routingService;
    }

    // Dobijanje svih provajdeera.
    @PreAuthorize("permitAll()")
    @RequestMapping(method = RequestMethod.GET, value = "/find")
    public ResponseEntity<List<ProviderDTO>> getAllProviders(@RequestHeader(required = true, value = Constants.HEADER_STRING) String jsonToken) {
        token = routingService.getTokenInfoByToken(jsonToken);

        if (token.getIsProvider())
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

        else
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());


        return new ResponseEntity<>(providerService.getAllProviders(), HttpStatus.OK);
    }

    // DOBIJANJE PROVAJDERA PO ID
    @PreAuthorize("permitAll()")
    @RequestMapping(method = RequestMethod.GET, value = "/find", params = "id")
    public ResponseEntity<ProviderDTO> findProviderById(@RequestParam(required = true, value = "id") Long id, @RequestHeader(required = true, value = Constants.HEADER_STRING) String jsonToken) {
        token = routingService.getTokenInfoByToken(jsonToken);

        if (token.getIsProvider())
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

        else
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());


        return new ResponseEntity<>(providerService.getProviderById(id), HttpStatus.OK);
    }

    // DOBIJANJE PROVAJDERA PO EMAIL
    @PreAuthorize("permitAll()")
    @RequestMapping(method = RequestMethod.GET, value = "/find", params = "email")
    public ResponseEntity<ProviderDTO> findProviderByEmail(@RequestParam(required = true, value = "email") String email, @RequestHeader(required = true, value = Constants.HEADER_STRING) String jsonToken) {
        token = routingService.getTokenInfoByToken(jsonToken);

        if (token.getIsProvider())
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

        else
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());


        return new ResponseEntity<>(providerService.getProviderByEmail(email), HttpStatus.OK);
    }

    // DOBIJANJE PROVAJDER PO USERNAME
    @PreAuthorize("permitAll()")
    @RequestMapping(method = RequestMethod.GET, value = "/find", params = "username")
    public ResponseEntity<ProviderDTO> findProviderByUsername(@RequestParam(required = true, value = "username") String username, @RequestHeader(required = true, value = Constants.HEADER_STRING) String jsonToken) {
        token = routingService.getTokenInfoByToken(jsonToken);

        if (token.getIsProvider())
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

        else
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());


        return new ResponseEntity<>(providerService.getProviderByUsername(username), HttpStatus.OK);
    }

    // REGISTRACIJA NOVOG PROVAJDERA
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<ProviderDTO> createProvider(@RequestBody ProviderDTO providerDTO) {


        return new ResponseEntity<>(providerService.createProvider(providerDTO), HttpStatus.OK);
    }

    // IZMENA POSTOJECEG PROVAJDERA
    @PreAuthorize("hasRole('PROVIDER')")
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH}, value = "/edit", params = "id")
    public ResponseEntity<ProviderDTO> updateProvider(@RequestParam(required = true, value = "id") Long id, @RequestBody ProviderDTO providerDTO) {
        if (providerDTO == null) {
            if (token.getIsProvider())
                routingService.createLogger(false, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
                }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

            else
                routingService.createLogger(false, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
                }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());

            return new ResponseEntity("Pristup je zabranjen!", HttpStatus.FORBIDDEN);
        }
        if (token.getIsProvider())
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

        else
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());

        return new ResponseEntity<>(providerService.updateProvider(id, providerDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @DeleteMapping(value = "/delete", params = "id")
    public ResponseEntity<Boolean> deleteProviderById(@RequestParam Long id) {

        if (token.getIsProvider())
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, token.getId(), null);

        else
            routingService.createLogger(true, true, this.getClass().getAnnotation(RequestMapping.class).value()[0], new Object() {
            }.getClass().getEnclosingMethod().getAnnotation(RequestMapping.class).value()[0], null, null, null, token.getId());


        return new ResponseEntity(providerService.deleteProviderById(id), HttpStatus.OK);
    }
}
