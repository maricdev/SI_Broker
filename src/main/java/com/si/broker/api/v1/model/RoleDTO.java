package com.si.broker.api.v1.model;

import lombok.Data;

import java.util.Date;

@Data
public class RoleDTO {

    private Long id;

    private String name;

    private Date createdAt;

    private Date updatedAt;
}
