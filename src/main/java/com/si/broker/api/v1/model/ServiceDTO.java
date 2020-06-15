package com.si.broker.api.v1.model;

import com.si.broker.model.Endpoint;
import com.si.broker.model.Provider;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ServiceDTO {

    private Long id;

    private String name;

    private String route;

    private boolean composite;

    private String description;

    private Long providers_id;

    private Date createdAt;

    private Date updatedAt;

    private Provider provider;

    private List<Endpoint> composite_services;

    private List<Endpoint> endpoints;


}
