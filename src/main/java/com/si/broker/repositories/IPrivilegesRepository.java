package com.si.broker.repositories;

import com.si.broker.model.User_Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPrivilegesRepository extends JpaRepository<User_Role, Long> {

    List<User_Role> findAll();

}
