package pers.zcc.scm.web.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.alipay.api.AlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.response.AlipayTradeQueryResponse;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Tested;
import pers.zcc.scm.common.dao.IUserDao;
import pers.zcc.scm.common.user.vo.UserVO;
import pers.zcc.scm.web.pay.alipay.config.AlipayConfig;
import pers.zcc.scm.web.pay.alipay.service.AlipayService;
import pers.zcc.scm.web.pay.order.OrderStatusEnum;
import pers.zcc.scm.web.pay.order.OrderVO;

/**
 * 运行时需要加虚拟机参数-javaagent:D:/JavaEEzhangchangchun/maven_repository/repository/org/jmockit/jmockit/1.49/jmockit-1.49.jar
 * @author zhangchangchun
 * @Date 2022年5月6日
 */
@DisplayName("测试样例")
class TestTest extends TestBase {

    @Inject
    private IUserDao userDao;

    @Inject
    AlipayService alipayService;

    @Tested
    AlipayService alipayService2;

    @Injectable
    private AlipayConfig config;

    @BeforeAll
    protected static void setUpBeforeClass() throws Exception {
        TestBase.setUpBeforeClass();
    }

    @AfterAll
    protected static void tearDownAfterClass() throws Exception {
        TestBase.tearDownAfterClass();
    }

    @Override
    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    @AfterEach
    protected void tearDown() throws Exception {
        super.tearDown();
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

    @Test
    /**
     * 各模块应该有自己的单元测试包，这里只是举个例子，应该在business模块测试这个方法
     * */
    void test4() {
        OrderVO order = new OrderVO();
        order.setTradeNo("O123_123");
        assertThrows(Exception.class, () -> {
            alipayService.queryOrderStatus(order);
        });
    }

    class MockDefaultAlipayClient extends MockUp<DefaultAlipayClient> {
        @Mock
        public void $init(String serverUrl, String appId, String privateKey, String format, String charset,
                String alipayPublicKey, String signType) {
            System.out.println("mock DefaultAlipayClient");
        }

        @Mock
        public AlipayTradeQueryResponse execute(AlipayRequest<AlipayTradeQueryResponse> request) {
            return null;
        }

    }

    @Test
    void test5() {
        String tradeNo = "O123_123";
        OrderVO order = new OrderVO();
        order.setTradeNo(tradeNo);
        MockDefaultAlipayClient mockDefaultAlipayClient = new MockDefaultAlipayClient();
        new Expectations(mockDefaultAlipayClient) {
            {
                AlipayTradeQueryResponse res = new AlipayTradeQueryResponse();
                res.setSubCode("");
                res.setOutTradeNo(tradeNo);
                res.setTradeStatus("TRADE_SUCCESS");
                mockDefaultAlipayClient.execute((AlipayRequest<AlipayTradeQueryResponse>) any);
                result = res;
            }
        };
        Integer res1 = alipayService2.queryOrderStatus(order);
        assertEquals(res1, OrderStatusEnum.PAY_SUCCESS.getValue());

        new Expectations(mockDefaultAlipayClient) {
            {
                AlipayTradeQueryResponse res = new AlipayTradeQueryResponse();
                res.setSubCode("failed");
                mockDefaultAlipayClient.execute((AlipayRequest<AlipayTradeQueryResponse>) any);
                result = res;
            }
        };
        Integer res2 = alipayService2.queryOrderStatus(order);
        assertEquals(res2, OrderStatusEnum.NOT_NOTIFIED.getValue());
    }

}
