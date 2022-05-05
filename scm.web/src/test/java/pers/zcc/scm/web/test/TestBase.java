/**
 * 
 */
package pers.zcc.scm.web.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pers.zcc.scm.common.constant.UserTypeEnum;
import pers.zcc.scm.common.dao.IUserDao;
import pers.zcc.scm.common.frame.UserCache;
import pers.zcc.scm.common.user.vo.UserVO;

/**
 * base junit test to be extended
 * @author zhangchangchun
 * @Date 2022年5月5日
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration({ "classpath:applicationContext.xml" })
@DisplayName("base test class set")
public class TestBase {

    static protected UserVO user;

    @Inject
    private IUserDao userDao;

    /**
     * @throws java.lang.Exception
     */
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        System.setProperty("spring.profiles.active", "dev");
        user = new UserVO();
        user.setId(1);
        user.setUserId(1);
        user.setUserName("root");
        user.setUserType(UserTypeEnum.SYSTEM.getValue());
        UserCache.setUser(user);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        UserCache.removeUser();
    }

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    @DisplayName("test1")
    void test() {
        UserVO res = userDao.findUser(user);
        assertNotNull(res);
    }

    @DisplayName("test2")
    @ParameterizedTest
    @ValueSource(ints = { 1, 2 })
    void test2(int num) {
        System.out.println(num);
    }

    @DisplayName("test3")
    @RepeatedTest(value = 2, name = "{displayName} run in time {currentRepetition}")
    void test3() {
        System.out.println(3);
    }

}
