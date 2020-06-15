package com.si.broker.model;

import lombok.Data;

import java.util.Date;

@Data
public class Token {

    private Long id;

    private String username;

    private String email;

    private Boolean isSaradnik;

    private Boolean isKorisnik;

    private Boolean isProvider;

    private Boolean isAdmin;

    private Long provider_id;

    private Date expiration;

}
