package com.si.broker.api.v1.model;

import com.si.broker.model.Service;
import com.si.broker.model.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProviderDTO {

    private Long id;

    private String name;

    private String host;

    private int port;

    private String email;

    private String username;

    private String password;

    private Date createdAt;

    private Date updatedAt;

    private List<User> users;

    private List<Service> services;

}