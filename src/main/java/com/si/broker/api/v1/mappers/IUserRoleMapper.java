package com.si.broker.api.v1.mappers;

import com.si.broker.api.v1.model.RoleDTO;
import com.si.broker.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper

public interface IUserRoleMapper {

    IUserRoleMapper instance = Mappers.getMapper(IUserRoleMapper.class);

    RoleDTO roleToRoleDTO(Role role);

    Role roleDtoToRole(RoleDTO roleDTO);

}
