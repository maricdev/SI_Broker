package com.si.broker.controllers.v1;

import com.si.broker.api.v1.model.RoleDTO;
import com.si.broker.services.IRoleService;
import com.si.broker.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(Constants.ROLES_BASE_URL)
public class RoleController {

    IRoleService userRoleService;

    @Autowired
    public RoleController(IRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/find")
    public ResponseEntity<List<RoleDTO>> getAllUserRoles() {

        return new ResponseEntity<>(userRoleService.getAllUserRoles(), HttpStatus.OK);
    }
}
