package com.si.broker.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class User_RoleKey implements Serializable {

    private Long roles_id;

    private Long users_id;
}
