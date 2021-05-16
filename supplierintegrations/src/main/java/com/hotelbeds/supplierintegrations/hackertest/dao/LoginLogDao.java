package com.hotelbeds.supplierintegrations.hackertest.dao;

import com.hotelbeds.supplierintegrations.hackertest.dao.repository.LoginLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LoginLogDao {

    @Autowired
    private LoginLogRepository loginLogRepository;

    public Integer countLoginLog (String ip, String action, LocalDateTime loginDate){
        return loginLogRepository.countByIdAndActionAndLoginDateGreaterThan(ip, action,loginDate);
    }
}
