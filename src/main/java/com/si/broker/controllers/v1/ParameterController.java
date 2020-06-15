package com.si.broker.controllers.v1;


import com.si.broker.api.v1.model.ParameterDTO;
import com.si.broker.services.IParameterService;
import com.si.broker.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(Constants.PARAMETERS_BASE_URL)
public class ParameterController {

    private final IParameterService parameterService;

    @Autowired
    public ParameterController(IParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @PreAuthorize("hasAnyRole({'ADMIN', 'KORISNIK', 'SARADNIK', 'PROVIDER'})")
    @RequestMapping(method = RequestMethod.GET, value = "/find")
    public ResponseEntity<List<ParameterDTO>> getAllParameters() {

        return new ResponseEntity<>(parameterService.getAllParametars(), HttpStatus.OK);
    }

    // REGISTRACIJA PARAMETRA
    @PreAuthorize("hasAnyRole({'ADMIN', 'PROVIDER'})")
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<ParameterDTO> createParameter(@RequestBody ParameterDTO parameterDTO) {
        return new ResponseEntity<>(parameterService.createParametar(parameterDTO), HttpStatus.OK);
    }

    // IZMENA PARAMETRA
    @PreAuthorize("hasAnyRole({'ADMIN', 'PROVIDER'})")
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH}, value = "/edit", params = "id")
    public ResponseEntity<ParameterDTO> updateParameter(@RequestParam(required = true, value = "id") Long id, @RequestBody ParameterDTO parameterDTO) {
        return new ResponseEntity<>(parameterService.updateParametar(id, parameterDTO), HttpStatus.OK);
    }

    // BRISANJE PARAMETRA
    @PreAuthorize("hasAnyRole({'ADMIN', 'PROVIDER'})")
    @DeleteMapping(value = "/delete", params = "id")
    public ResponseEntity<Boolean> deleteParameterById(@RequestParam Long id) {
        return new ResponseEntity(parameterService.deleteParametarById(id), HttpStatus.OK);
    }
}
