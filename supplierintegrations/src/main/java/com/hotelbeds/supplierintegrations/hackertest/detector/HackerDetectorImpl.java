package com.hotelbeds.supplierintegrations.hackertest.detector;

import com.hotelbeds.supplierintegrations.hackertest.dao.LoginLogDao;
import com.hotelbeds.supplierintegrations.hackertest.model.UserDataLogin;
import com.hotelbeds.supplierintegrations.hackertest.model.enumerate.Action;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HackerDetectorImpl implements HackerDetector {

    @Value("${data.ipPosition}")
    Integer ipPosition;
    @Value("${data.datePosition}")
    Integer datePosition;
    @Value("${data.actionPosition}")
    Integer actionPosition;
    @Value("${data.namePosition}")
    Integer namePosition;
    @Value("${data.hackMinutesthreshold}")
    Integer hackMinutesthreshold;
    @Value("${data.hackTriesthreshold}")
    Integer hackTriesthreshold;

    private static final String COMMA = ",";

    @Autowired
    private LoginLogDao loginLogDao;

    public String parseLine(String line) {

        String result = null;

        UserDataLogin userDataLogin = createUserParam(line);

        if (Action.SIGNIN_FAILURE.toString().equals(userDataLogin.getAction().toString())) {
            Integer countLoginLog = loginLogDao.countLoginLog(
                    userDataLogin.getIp(),
                    userDataLogin.getAction().toString(),
                    addMinutes(userDataLogin.getLoginDate(), hackMinutesthreshold)
            );

            if (countLoginLog >= hackTriesthreshold){
                result = userDataLogin.getIp();
            }

        }

        return result;
    }

    private LocalDateTime addMinutes (LocalDateTime loginDate, int minutes) {
        return loginDate.plusMinutes(minutes);
    }

    private UserDataLogin createUserParam(String line) {
        List<String> splitLine = Arrays.stream(line.split(COMMA)).collect(Collectors.toList());

        UserDataLogin userDataLogin = new UserDataLogin();

        AtomicInteger cont = new AtomicInteger();
        splitLine.forEach(split -> {
                    if (cont.get() == ipPosition) {
                        userDataLogin.setIp(split.trim());
                    } else if (cont.get() == datePosition) {
                        userDataLogin.setLoginDate(longToDate(split.trim()));
                    } else if (cont.get() == actionPosition) {
                        userDataLogin.setAction(Action.SIGNIN_SUCCESS.toString().equals(split.trim())
                                ? Action.SIGNIN_SUCCESS : Action.SIGNIN_FAILURE);

                    } else if (cont.get() == namePosition) {
                        userDataLogin.setName(split.trim());
                    } else {
                        log.error("Not found configuration for position [{}]", cont);
                    }
                    cont.getAndIncrement();
                }
        );

        return userDataLogin;
    }

    private LocalDateTime longToDate(String split) {
        return Instant.ofEpochMilli(Long.parseLong(split)).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
