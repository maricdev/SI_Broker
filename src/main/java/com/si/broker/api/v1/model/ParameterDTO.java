package com.si.broker.api.v1.model;

import com.si.broker.model.Endpoint;
import lombok.Data;

import java.util.Date;

@Data
public class ParameterDTO {

    private Long id;

    private String type;

    private String valueType;

    private String name;

    private boolean mandatory;

    private boolean outSchema;

    private String description;

    private Endpoint endpoint;

    private Date createdAt;

    private Date updatedAt;

    private Long endpoints_id;

}
