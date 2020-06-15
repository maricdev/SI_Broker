package com.si.broker.services.implementations;

import com.si.broker.BrokerApplication;
import com.si.broker.api.v1.mappers.IUserMapper;
import com.si.broker.api.v1.model.ProviderDTO;
import com.si.broker.api.v1.model.UserDTO;
import com.si.broker.model.Token;
import com.si.broker.model.User;
import com.si.broker.repositories.IUserRepository;
import com.si.broker.services.IEndpointService;
import com.si.broker.services.IProviderService;
import com.si.broker.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IUserMapper userMapper;
    private final IProviderService providerService;
    private final IEndpointService endpointService;

    @Autowired
    public UserServiceImpl(IUserRepository userRepository, IUserMapper userMapper, IProviderService providerService, IEndpointService endpointService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.providerService = providerService;
        this.endpointService = endpointService;
    }

    //GET
    public List<UserDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getAllUsersByProviderId(Token token) {
        List<User> users = userRepository.findAllByProviderId(token.getProvider_id());
        List<User> usersToReturn = new ArrayList<>();
        if (token.getIsProvider() || token.getIsSaradnik() || token.getIsAdmin())
            return userMapper.userListToUserListDTO(users);
        else {
            usersToReturn.add(getUserByUsername(token.getUsername()));
            return userMapper.userListToUserListDTO(usersToReturn);
        }
    }

    public UserDTO getUserDtoByIdByPrivilege(Long id, Token token) {
        UserDTO user = getUserDtoById(id);
        if (user.getProviders_id() == token.getProvider_id())
            return getUserDtoById(id);
        else
            return null;
    }

    public UserDTO getUserByEmailByPrivilege(String email, Token token) {
        UserDTO user = getUserByEmail(email);
        if (user.getProviders_id() == token.getProvider_id())
            return getUserByEmail(email);
        else
            return null;
    }

    public UserDTO getUserDtoByUsernameByPrivilege(String username, Token token) {
        UserDTO user = getUserDtoByUsername(username);
        if (user.getProviders_id() == token.getProvider_id())
            return getUserDtoByUsername(username);
        else
            return null;
    }

    @Override
    public UserDTO getUserDtoById(Long id) {
        return userMapper.userToUserDTO(userRepository.getOne(id));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return userMapper.userToUserDTO(userRepository.findByEmail(email));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    public UserDTO getUserDtoByUsername(String username) {
        return userMapper.userToUserDTO(userRepository.findByUsername(username));
    }


    //POST
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        List<UserDTO> users = getAllUsers();
        boolean exist = false;
        for (UserDTO u : users) {
            if (u.getEmail().equals(userDTO.getEmail())) {
                exist = true;
                break;
            } else if (u.getUsername().equals(userDTO.getUsername())) {
                exist = true;
                break;
            }
        }
        List<ProviderDTO> providers = providerService.getAllProviders();
        for (ProviderDTO p : providers) {
            if (p.getEmail().equals(userDTO.getEmail())) {
                exist = true;
                break;
            } else if (p.getUsername().equals(userDTO.getUsername())) {
                exist = true;
                break;
            }
        }
        if (exist == true)
            return null;
        userDTO.setPassword(BrokerApplication.bCryptPasswordEncoder().encode(userDTO.getPassword()));
        return saveAndReturnDTO(userMapper.userDtoToUser(userDTO));
    }


    public Boolean deleteUserById(Long id, Token token) {
        UserDTO user = getUserDtoById(id);

        if ((token.getIsAdmin() && token.getProvider_id() == user.getProviders_id()) ||
                (token.getIsProvider() && token.getProvider_id() == user.getProviders_id()) ||
                (token.getId() == id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public UserDTO updateUser(long id, UserDTO userDTO, Token token) {
        UserDTO user = getUserDtoById(id);

        if ((token.getIsAdmin() && token.getProvider_id() == user.getProviders_id()) ||
                (token.getIsProvider() && token.getProvider_id() == user.getProviders_id()) ||
                (token.getId() == id)) {

            User inRepository = userRepository.getOne(id);

            User customerToSave = userMapper.userDtoToUser(userDTO);
            if (inRepository == null)
                return null;

            if (customerToSave.getFirstName() == null)
                customerToSave.setFirstName(inRepository.getFirstName());
            if (customerToSave.getLastName() == null)
                customerToSave.setLastName(inRepository.getLastName());
            if (customerToSave.getEmail() == null)
                customerToSave.setEmail(inRepository.getEmail());
            if (customerToSave.getUsername() == null)
                customerToSave.setUsername(inRepository.getUsername());
            if (customerToSave.getPassword() == null)
                customerToSave.setPassword(inRepository.getPassword());
            else
                customerToSave.setPassword(BrokerApplication.bCryptPasswordEncoder().encode(userDTO.getPassword()));

            customerToSave.setId(id);

            return saveAndReturnDTO(customerToSave);
        }
        return null;
    }

    //Utils
    private UserDTO saveAndReturnDTO(User user) {
        userRepository.save(user);
        User u = userRepository.findByUsername(user.getUsername());

        UserDTO toReturn = userMapper.userToUserDTO(u);
        return toReturn;

    }
}
