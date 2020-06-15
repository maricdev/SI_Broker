package com.si.broker.repositories;

import com.si.broker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserRepository extends JpaRepository<User, Long> {

    List<User> findAllByProviderId(Long id);

    User findByUsername(String username);

    User findByEmail(String email);

}
