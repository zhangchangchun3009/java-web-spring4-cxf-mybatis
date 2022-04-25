
package pers.zcc.scm.common.frame.privillege;

/**
 * The Class NoPrivilegeException.
 *
 * @author zhangchangchun
 * @since 2021年4月6日
 */
public class NoPrivilegeException extends RuntimeException {

    private static final long serialVersionUID = 7853278004556933768L;

    public NoPrivilegeException() {
        super();
    }

    public NoPrivilegeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NoPrivilegeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPrivilegeException(String message) {
        super(message);
    }

    public NoPrivilegeException(Throwable cause) {
        super(cause);
    }

}
