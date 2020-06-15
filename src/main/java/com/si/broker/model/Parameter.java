package com.si.broker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Parameter")
@Table(name = "PARAMETERS")
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
@EntityListeners(AuditingEntityListener.class)
@Data
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String type;

    @Column
    private String valueType;

    @Column
    private String name;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean mandatory;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean outSchema;

    @Column
    private String description;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatedAt", nullable = false)
    private Date updatedAt;

    @JsonIgnore
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "endpoints_id", insertable = false, updatable = false)
    private Endpoint endpoint;

    @JsonIgnore
    @Column
    private Long endpoints_id;

}
