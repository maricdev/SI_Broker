package com.si.broker.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Composite_ServiceKey implements Serializable {

    private Long services_id;

    private Long endpoints_id;

    private Integer number;

}
