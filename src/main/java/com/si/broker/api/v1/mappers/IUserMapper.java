package com.si.broker.api.v1.mappers;

import com.si.broker.api.v1.model.UserDTO;
import com.si.broker.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface IUserMapper {

    IUserMapper instance = Mappers.getMapper(IUserMapper.class);

    UserDTO userToUserDTO(User user);

    User userDtoToUser(UserDTO userDTO);

    List<UserDTO> userListToUserListDTO(List<User> users);

    List<User> userListDtoToUserList(List<UserDTO> userDTO);

}
