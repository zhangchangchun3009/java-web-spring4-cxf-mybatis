
package pers.zcc.scm.common.frame;

/**
 * 項目啓動后執行動作的接口
 *
 * @author zhangchangchun
 * @since 2021年4月21日
 */
public interface IAfterStartUpHandle {

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
