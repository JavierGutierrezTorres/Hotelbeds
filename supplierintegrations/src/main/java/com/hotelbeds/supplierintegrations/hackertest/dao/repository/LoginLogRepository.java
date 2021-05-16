package com.hotelbeds.supplierintegrations.hackertest.dao.repository;

import com.hotelbeds.supplierintegrations.hackertest.dao.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoginLogRepository extends CrudRepository<LoginLog, Long>, JpaSpecificationExecutor<LoginLog> {

    Integer countByIdAndActionAndLoginDateGreaterThan(String ip, String action, Date loginDate);
}
