package com.hotelbeds.supplierintegrations.hackertest.detector

import com.hotelbeds.supplierintegrations.hackertest.dao.LoginLogDao
import com.hotelbeds.supplierintegrations.hackertest.exception.ErrorParseDate
import com.hotelbeds.supplierintegrations.hackertest.exception.IncorrectUserLogLine
import com.hotelbeds.supplierintegrations.hackertest.model.enumerate.Action
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class HackerDetectorImplUnitSpec extends Specification{

    @Subject
    private HackerDetectorImpl hackerDetector

    private LoginLogDao loginLogDao = Mock(LoginLogDao)

    private Integer ipPosition = 0;
    private Integer datePosition = 1;
    private Integer actionPosition = 2;
    private Integer namePosition = 3;
    private Integer hackMinutesthreshold = -5;
    private Integer hackTriesthreshold = 5;

    private final static String IP = "80.238.9.179"
    private final static String DATE_IN_MILISECONS = "133612947"
    private final static String NAME = "Will.Smith"

    private final static String SUCCESS_LINE ="${IP}, ${DATE_IN_MILISECONS}, ${Action.SIGNIN_SUCCESS}, ${NAME}"
    private final static String FAILURE_LINE ="${IP}, ${DATE_IN_MILISECONS}, ${Action.SIGNIN_FAILURE}, ${NAME}"
    private final static String INCOMPLETE_LINE ="${IP}, ${DATE_IN_MILISECONS}, ${Action.SIGNIN_FAILURE}"

    private final static String RFC2822_DATE_EXAMPLE_ONE = "01 Sep 2016 10:11:12 +0000"
    private final static String RFC2822_DATE_EXAMPLE_TWO = "01 Sep 2016 11:11:12 +0000"

    def setup() {
        hackerDetector = new HackerDetectorImpl(
                loginLogDao: loginLogDao,
                ipPosition: ipPosition,
                datePosition: datePosition,
                actionPosition: actionPosition,
                namePosition: namePosition,
                hackMinutesthreshold: hackMinutesthreshold,
                hackTriesthreshold: hackTriesthreshold
        )
    }

    def 'Read line with action SUCCES'() {
        given:
            String line = SUCCESS_LINE
        when:
            String result = hackerDetector.parseLine(line)
        then:
            result == null
    }

    def 'Read line with action FAIL, but less than hackTriesthreshold'() {
        given:
            String line = FAILURE_LINE
        when:
            String result = hackerDetector.parseLine(line)
        then:
            1 * loginLogDao.countLoginLog (_ as String, _ as String, _ as LocalDateTime) >> (hackTriesthreshold - 1)
            result == null
    }

    def 'Read line with action FAIL, and return IP'() {
        given:
            String line = FAILURE_LINE
        when:
            String result = hackerDetector.parseLine(line)
        then:
            1 * loginLogDao.countLoginLog (_ as String, _ as String, _ as LocalDateTime) >> hackTriesthreshold
            IP == result
    }

    def 'Read incomplete line'() {
        given:
            String line = INCOMPLETE_LINE
        when:
            hackerDetector.parseLine(line)
        then:
            thrown(IncorrectUserLogLine)
    }

    def 'Compare two date, but parse is incorrect' () {
        when:
            hackerDetector.timeCalculation(RFC2822_DATE_EXAMPLE_ONE, RFC2822_DATE_EXAMPLE_TWO)
        then:
            thrown(ErrorParseDate)
    }

}
