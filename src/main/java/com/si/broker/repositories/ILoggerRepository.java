package com.si.broker.repositories;

import com.si.broker.model.Logger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILoggerRepository extends JpaRepository<Logger, Long> {


}
