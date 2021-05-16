package com.hotelbeds.supplierintegrations.hackertest.detector

import com.hotelbeds.supplierintegrations.hackertest.dao.LoginLogDao
import spock.lang.Specification
import spock.lang.Subject

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
            String line = "80.238.9.179,133612947,SIGNIN_SUCCESS,Will.Smith";
        when:
            String result = hackerDetector.parseLine(line)
        then:
            result == null
    }

    def 'Read line with action FAIL, but less than hackTriesthreshold'() {
        given:
            String line = "80.238.9.179,133612947,SIGNIN_FAILURE,Will.Smith";
        when:
            String result = hackerDetector.parseLine(line)
        then:
            1 * loginLogDao.countLoginLog (_ as String, _ as String, _ as Date) >> (hackTriesthreshold - 1)
            result == null
    }

    def 'Read line with action FAIL, and return IP'() {
        given:
            String line = "80.238.9.179,133612947,SIGNIN_FAILURE,Will.Smith";
        when:
            String result = hackerDetector.parseLine(line)
        then:
            1 * loginLogDao.countLoginLog (_ as String, _ as String, _ as Date) >> hackTriesthreshold
            "80.238.9.179".equals(result)
    }

}
