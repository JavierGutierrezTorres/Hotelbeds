package com.hotelbeds.supplierintegrations.hackertest.dao.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LOGIN_LOG")
public class LoginLog {

    @Id
    @GeneratedValue(generator = "LOG_LOGIN_SEQ")
    @SequenceGenerator(name = "LOG_LOGIN_SEQ", sequenceName = "LOG_LOGIN_SEQ", allocationSize = 1)
    @Column(name = "AUTOKEY")
    private Long id;

    @Column(name = "IP", columnDefinition = "varchar2(12)")
    @NotNull
    private String ip;

    @Column(name = "LOGIN_DATE")
    @NotNull
    private LocalDateTime loginDate;

    @Column(name = "ACTION", columnDefinition = "varchar2(30)")
    @NotNull
    private String action;

    @Column(name = "USER_NAME", columnDefinition = "varchar2(50)")
    @NotNull
    private String userName;

}
