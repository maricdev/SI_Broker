package com.si.broker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Logger")
@Table(name = "LOGGER")
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
@EntityListeners(AuditingEntityListener.class)
@Data
public class Logger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatedAt", nullable = false)
    private Date updatedAt;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isSuccess;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isBroker;

    @Column
    private String services_path;

    @Column
    private String endpoints_path;

    @Column
    private Long services_id;

    @Column
    private Long endpoints_id;

    @Column
    private Long providers_id;

    @Column
    private Long users_id;

}
