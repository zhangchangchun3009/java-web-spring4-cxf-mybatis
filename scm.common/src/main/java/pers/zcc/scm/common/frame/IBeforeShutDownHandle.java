
package pers.zcc.scm.common.frame;

/**
 * 英语关闭前执行的动作接口
 *
 * @author zhangchangchun
 * @since 2021年4月21日
 */
public interface IBeforeShutDownHandle {
    /**
     * 执行动作
     */
    void process();

    /**
     * 执行顺序，0最先执行
     * 
     * @return int
     */
    int getOrder();
}
