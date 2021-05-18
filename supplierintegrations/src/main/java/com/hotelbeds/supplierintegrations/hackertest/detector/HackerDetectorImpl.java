package com.hotelbeds.supplierintegrations.hackertest.detector;

import com.hotelbeds.supplierintegrations.hackertest.dao.LoginLogDao;
import com.hotelbeds.supplierintegrations.hackertest.exception.EmptyDate;
import com.hotelbeds.supplierintegrations.hackertest.exception.ErrorParseDate;
import com.hotelbeds.supplierintegrations.hackertest.exception.IncorrectUserLogLine;
import com.hotelbeds.supplierintegrations.hackertest.model.UserDataLogin;
import com.hotelbeds.supplierintegrations.hackertest.model.enumerate.Action;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    private static final String RFC2822_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";

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

            if (countLoginLog >= hackTriesthreshold) {
                result = userDataLogin.getIp();
            }

        }

        return result;
    }

    public Long timeCalculation(String startDateString, String endDateString) {

        if (startDateString == null || endDateString == null) {
            throw new EmptyDate();
        }

        LocalDateTime startLocalDateTime, endLocalDateTime;

        try {
            startLocalDateTime = getDate(startDateString, RFC2822_PATTERN);
        } catch (DateTimeParseException dtp) {
            throw new ErrorParseDate("Can't parse ".concat(startDateString));
        }
        try {
            endLocalDateTime = getDate(endDateString, RFC2822_PATTERN);
        } catch (DateTimeParseException dtp) {
            throw new ErrorParseDate("Can't parse ".concat(endDateString));
        }

        long millis = Duration.between(startLocalDateTime, endLocalDateTime).toMillis();

        return TimeUnit.MILLISECONDS.toMinutes(millis);


    }

    private LocalDateTime getDate(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        return LocalDateTime.parse(dateString, formatter);
    }

    private LocalDateTime addMinutes(LocalDateTime loginDate, int minutes) {
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

        if (!userDataLogin.isAllDataInform()) {
            throw new IncorrectUserLogLine("LogLine doesn't have all data");
        }

        return userDataLogin;
    }

    private LocalDateTime longToDate(String split) {
        return Instant.ofEpochMilli(Long.parseLong(split)).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
