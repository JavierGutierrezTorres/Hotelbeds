package com.hotelbeds.supplierintegrations.hackertest.detector;

import com.hotelbeds.supplierintegrations.hackertest.domain.User;
import com.hotelbeds.supplierintegrations.hackertest.domain.enumerate.Action;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HackerDetectorImpl implements HackerDetector {

    private static final String COMMA = ",";

    private static final String DOT = ".";

    @Value("${data.ipPosition}")
    Integer ipPosition;
    @Value("${data.datePosition}")
    Integer datePosition;
    @Value("${data.actionPosition}")
    Integer actionPosition;
    @Value("${data.namePosition}")
    Integer namePosition;


    public String parseLine(String line) {

        User user = createUserParam(line);

        //TODO check the IP

        return null;
    }

    private User createUserParam(String line) {
        List<String> splitLine = Arrays.stream(line.split(COMMA)).collect(Collectors.toList());

        User user = new User();

        int cont = 0;
        splitLine.forEach(split -> {
                    if (cont == ipPosition) {
                        user.setIp(split);
                    } else if (cont == datePosition) {
                        user.setLoginDate(longToDate(split));
                    } else if (cont == actionPosition) {
                        user.setAction(Action.SIGNIN_SUCCESS.toString().equals(split)
                                ? Action.SIGNIN_SUCCESS : Action.SIGNIN_FAILURE);

                    } else if (cont == namePosition) {
                        user.setName(split);
                    } else {
                        log.error("Not found configuration for position [{}]", cont);
                    }
                }
        );

        return user;
    }

    private Date longToDate(String split) {
        return new Date(TimeUnit.SECONDS.toMillis(Long.parseLong(split)));
    }

}
