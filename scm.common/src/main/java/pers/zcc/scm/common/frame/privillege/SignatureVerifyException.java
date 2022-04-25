
package pers.zcc.scm.common.frame.privillege;

/**
 * The Class AuthenticationException.
 * 
 * @author 张常春
 * @since 2021年6月11日
 */
public class SignatureVerifyException extends RuntimeException {
    private static final long serialVersionUID = -33276830712951196L;

    public SignatureVerifyException() {
        super();
    }

    public SignatureVerifyException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SignatureVerifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignatureVerifyException(String message) {
        super(message);
    }

    public SignatureVerifyException(Throwable cause) {
        super(cause);
    }

}
