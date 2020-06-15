package com.si.broker.api.v1.model;

import com.si.broker.model.Provider;
import com.si.broker.model.Role;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private String password;

    private Date createdAt;

    private Date updatedAt;

    private Long providers_id;

    private Provider provider;

    private List<Role> Roles;

}
