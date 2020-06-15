package com.si.broker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "Services")
@Table(name = "SERVICES")
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
@EntityListeners(AuditingEntityListener.class)

@Data
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "COMPOSITE_SERVICES",
            joinColumns = {@JoinColumn(name = "services_id")},
            inverseJoinColumns = {@JoinColumn(name = "endpoints_id")}
    )
    List<Endpoint> composite_services;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean composite;

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

    @Column
    private Long providers_id;
    @Column
    private String route;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "providers_id", insertable = false, updatable = false)
    private Provider provider;

    @JsonIgnore
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(
            mappedBy = "service",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Endpoint> endpoints;

}
