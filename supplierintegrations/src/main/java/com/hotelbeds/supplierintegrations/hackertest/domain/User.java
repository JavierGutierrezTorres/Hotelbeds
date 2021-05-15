package com.hotelbeds.supplierintegrations.hackertest.domain;

import com.hotelbeds.supplierintegrations.hackertest.domain.enumerate.Action;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class User {

    private String ip;

    private Date loginDate;

    private Action action;

    private String name;

}
