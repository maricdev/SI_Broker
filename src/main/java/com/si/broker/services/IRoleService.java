package com.si.broker.services;

import com.si.broker.api.v1.model.RoleDTO;

import java.util.List;

public interface IRoleService {

    List<RoleDTO> getAllUserRoles();

}
