package com.si.broker.services;

import com.si.broker.api.v1.model.UserDTO;
import com.si.broker.model.Token;
import com.si.broker.model.User;

import java.util.List;

public interface IUserService {

    //GET
    List<UserDTO> getAllUsers();

    List<UserDTO> getAllUsersByProviderId(Token token);

    UserDTO getUserDtoById(Long id);

    UserDTO getUserByEmail(String email);

    UserDTO getUserDtoByUsername(String username);

    User getUserByUsername(String username);

    //PUT
    UserDTO createUser(UserDTO userDTO);

    Boolean deleteUserById(Long id, Token token);

    UserDTO updateUser(long id, UserDTO userDTO, Token token);

    UserDTO getUserDtoByIdByPrivilege(Long id, Token token);

    UserDTO getUserByEmailByPrivilege(String email, Token token);

    UserDTO getUserDtoByUsernameByPrivilege(String username, Token token);

}
