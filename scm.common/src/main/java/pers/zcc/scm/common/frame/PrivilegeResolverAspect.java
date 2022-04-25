
package pers.zcc.scm.common.frame;

import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pers.zcc.scm.common.constant.UserTypeEnum;
import pers.zcc.scm.common.dao.IUserDao;
import pers.zcc.scm.common.frame.privillege.NoPrivilegeException;
import pers.zcc.scm.common.frame.privillege.Privilege;
import pers.zcc.scm.common.frame.privillege.Resource;
import pers.zcc.scm.common.frame.privillege.SignatureVerifyException;
import pers.zcc.scm.common.user.vo.ResourceVO;
import pers.zcc.scm.common.user.vo.UserVO;
import pers.zcc.scm.common.util.APISignUtil;
import pers.zcc.scm.common.util.HttpContextManager;

/**
 * The Class PrivilegeResolverAspect.
 *
 * @author zhangchangchun
 * @since 2021年4月6日
 */
@Named
@Aspect
public class PrivilegeResolverAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrivilegeResolverAspect.class);

    private ThreadLocal<Boolean> reentrantLock = ThreadLocal.withInitial(() -> false);

    @Inject
    private IUserDao userDao;

    @Pointcut("execution((!void) pers.zcc..service.impl.*.*(..))")
    public void pointCut() {
    }

    @Pointcut("execution(void pers.zcc..service.impl.*.*(..))")
    public void pointCutNoReturn() {
    }

    @Around("pointCutNoReturn()")
    public void aroundNoReturn(ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.debug("aop around " + joinPoint);
        HttpServletRequest request = HttpContextManager.getRequest();
        if (request == null) {
            joinPoint.proceed();
            return;
        }
        if (reentrantLock.get()) {
            joinPoint.proceed();
            return;
        }
        boolean hasPrivilege = privilegeResolve(joinPoint);
        if (!hasPrivilege) {
            throw new NoPrivilegeException(joinPoint.toString());
        }
        boolean signCheckSuccess = verifyPublicServicesCall(joinPoint);
        if (!signCheckSuccess) {
            throw new SignatureVerifyException(joinPoint.toString());
        }
        try {
            reentrantLock.set(true);
            joinPoint.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            reentrantLock.remove();
            UserCache.removeUser();
        }
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        LOGGER.debug("aop around " + joinPoint);
        HttpServletRequest request = HttpContextManager.getRequest();
        if (request == null) {
            return joinPoint.proceed();
        }
        if (reentrantLock.get()) {
            return joinPoint.proceed();
        }
        boolean hasPrivilege = privilegeResolve(joinPoint);
        if (!hasPrivilege) {
            throw new NoPrivilegeException(joinPoint.toString());
        }
        boolean signCheckSuccess = verifyPublicServicesCall(joinPoint);
        if (!signCheckSuccess) {
            throw new SignatureVerifyException(joinPoint.toString());
        }
        Object result = null;
        try {
            reentrantLock.set(true);
            result = joinPoint.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            reentrantLock.remove();
            UserCache.removeUser();
        }
        return result;
    }

    private boolean privilegeResolve(ProceedingJoinPoint joinPoint) {
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        Method met = sig.getMethod();
        Privilege pl = met.getDeclaredAnnotation(Privilege.class); // 因为这行代理必须使用cglib（否则权限注解需要打在接口上）
        Object service = joinPoint.getTarget();
        Resource re = service.getClass().getAnnotation(Resource.class);
        if (pl == null || re == null) {
            return true;
        }
        UserVO user = HttpContextManager.getUser();
        if (user == null) {
            return false;
        }
        if (UserTypeEnum.SYSTEM.getValue().equalsIgnoreCase(user.getUserType())) {
            return true;
        }
        String privilegeCode = pl.code();
        ResourceVO param = new ResourceVO();
        param.setMethodCode(privilegeCode);
        String resourceCode = re.code();
        String resourceDesc = re.desc();
        String[] rda = resourceDesc.split(" ");
        String moduelName = null;
        if (rda.length != 1) {
            moduelName = rda[0];
        }
        param.setModuleName(moduelName);
        param.setServiceCode(resourceCode);
        int existance = userDao.hasPrivilege(user, param);
        return existance >= 1;
    }

    private boolean verifyPublicServicesCall(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = HttpContextManager.getRequest();
        String path = request.getRequestURI();
        if (!path.contains("/publicservices/")) {
            return true;
        }
        String appId = request.getHeader("appId");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String publicKey = APISignUtil.getAppPublicKey(appId);
        if (appId == null || timestamp == null || sign == null) {
            return false;
        }
        Long timestampL = Long.parseLong(timestamp);
        Object parameters = joinPoint.getArgs();
        try {
            return APISignUtil.verify(appId, timestampL, parameters, sign, publicKey);
        } catch (Exception e) {
            LOGGER.error("APISignUtil.verify e,", e);
        }
        return false;
    }

}
