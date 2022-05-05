package pers.zcc.scm.common.frame;

import pers.zcc.scm.common.user.vo.UserVO;

public class UserCache {

    private static ThreadLocal<UserVO> userThreadLocal = new ThreadLocal<UserVO>();

    /**
     * Gets the user.业务代码可调用
     *
     * @return the user
     */
    public static UserVO getUser() {
        return userThreadLocal.get();
    }

    /**
     * 禁止调用
     * @param user
     */
    public static void setUser(UserVO user) {
        userThreadLocal.set(user);
    }

    /**
     * 禁止调用
     */
    public static void removeUser() {
        userThreadLocal.remove();
    }

}
