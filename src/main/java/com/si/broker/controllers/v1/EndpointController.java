package com.si.broker.controllers.v1;

import com.si.broker.api.v1.model.EndpointDTO;
import com.si.broker.services.IEndpointService;
import com.si.broker.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(Constants.ENDPOINTS_BASE_URL)

public class EndpointController {
    private final IEndpointService endpointService;


    @Autowired
    public EndpointController(IEndpointService endpointService) {
        this.endpointService = endpointService;
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(method = RequestMethod.GET, value = "/find")
    public ResponseEntity<List<EndpointDTO>> getAllEndpoints() {


        return new ResponseEntity<>(endpointService.getAllEndpoints(), HttpStatus.OK);
    }

    // REGISTRACIJA ENDPOINTA
    @PreAuthorize("hasAnyRole({'ADMIN', 'PROVIDER'})")
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<EndpointDTO> createEndpoint(@RequestBody EndpointDTO endpointDTO) {
        return new ResponseEntity<>(endpointService.createEndpoint(endpointDTO), HttpStatus.OK);
    }

    // IZMENA ENDPOINTA
    @PreAuthorize("hasAnyRole({'ADMIN', 'PROVIDER'})")
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH}, value = "/edit", params = "id")
    public ResponseEntity<EndpointDTO> updateEndpoint(@RequestParam(required = true, value = "id") Long id, @RequestBody EndpointDTO endpointDTO) {
        return new ResponseEntity<>(endpointService.updateEndpoint(id, endpointDTO), HttpStatus.OK);
    }

    // BRISANJE ENDPOINTA
    @DeleteMapping(value = "/delete", params = "id")
    @PreAuthorize("hasAnyRole({'ADMIN', 'PROVIDER'})")
    public ResponseEntity<Boolean> deleteEndpointById(@RequestParam Long id) {
        return new ResponseEntity(endpointService.deleteEndpointById(id), HttpStatus.OK);
    }
}
