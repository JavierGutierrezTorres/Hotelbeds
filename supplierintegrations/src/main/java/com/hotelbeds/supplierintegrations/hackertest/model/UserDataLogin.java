package com.hotelbeds.supplierintegrations.hackertest.model;

import com.hotelbeds.supplierintegrations.hackertest.model.enumerate.Action;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserDataLogin {

    private String ip;

    private LocalDateTime loginDate;

    private Action action;

    private String name;

}
