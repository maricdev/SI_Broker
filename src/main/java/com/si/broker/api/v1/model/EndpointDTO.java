package com.si.broker.api.v1.model;

import com.si.broker.model.Endpoint_Role;
import com.si.broker.model.Parameter;
import com.si.broker.model.Service;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EndpointDTO {

    private Long id;

    private String name;

    private String type;

    private boolean isAvailable;

    private boolean isPublic;

    private String route;

    private String description;

    private String method;

    private Long services_id;

    private Date createdAt;

    private Date updatedAt;

    private Service service;

    private List<Parameter> parameters;

//    private List<Role> roles;

    private List<Endpoint_Role> permissions;
}