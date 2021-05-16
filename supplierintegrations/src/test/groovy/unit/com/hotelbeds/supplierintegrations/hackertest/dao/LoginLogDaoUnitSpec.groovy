package com.hotelbeds.supplierintegrations.hackertest.dao

import com.hotelbeds.supplierintegrations.hackertest.dao.repository.LoginLogRepository
import com.hotelbeds.supplierintegrations.hackertest.model.enumerate.Action
import spock.lang.Specification
import spock.lang.Subject

class LoginLogDaoUnitSpec extends Specification {

    @Subject
    private LoginLogDao loginLogDao

    private LoginLogRepository loginLogRepository = Mock(LoginLogRepository)

    private final static String IP = "80.238.9.179"

    def setup() {
        loginLogDao = new LoginLogDao(
                loginLogRepository: loginLogRepository
        )
    }

    def 'Try count login log'() {
        when:
            loginLogDao.countLoginLog(IP, Action.SIGNIN_SUCCESS.toString(), new Date())
        then:
            1 * loginLogRepository.countByIdAndActionAndLoginDateGreaterThan(
                    IP, Action.SIGNIN_SUCCESS.toString(), _ as Date
            )

    }
}
