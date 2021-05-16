package com.hotelbeds.supplierintegrations.hackertest.model;

import com.hotelbeds.supplierintegrations.hackertest.model.enumerate.Action;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserDataLogin {

    private String ip;

    private Date loginDate;

    private Action action;

    private String name;

}
