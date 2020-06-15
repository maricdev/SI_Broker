package com.si.broker.services.implementations;


import com.si.broker.api.v1.mappers.IUserRoleMapper;
import com.si.broker.api.v1.model.RoleDTO;
import com.si.broker.repositories.IRoleRepository;
import com.si.broker.services.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class RoleServiceImpl implements IRoleService {

    private final IRoleRepository userRoleRepository;
    private final IUserRoleMapper userRoleMapper;

    @Autowired
    public RoleServiceImpl(IRoleRepository userRoleRepository, IUserRoleMapper userRoleMapper) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public List<RoleDTO> getAllUserRoles() {
        return userRoleRepository
                .findAll()
                .stream()
                .map(userRoleMapper::roleToRoleDTO).collect(Collectors.toList());
    }


}
