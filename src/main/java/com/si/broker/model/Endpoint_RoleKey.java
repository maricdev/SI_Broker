package com.si.broker.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Endpoint_RoleKey implements Serializable {

    private Long endpoint;

    private Long role;
}
