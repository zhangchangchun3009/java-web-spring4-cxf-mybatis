
package pers.zcc.scm.common.frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pers.zcc.scm.common.constant.UserTypeEnum;
import pers.zcc.scm.common.dao.IUserDao;
import pers.zcc.scm.common.user.vo.UserVO;
import pers.zcc.scm.common.util.ApplicationContextManager;
import pers.zcc.scm.common.util.EnvironmentProps;
import pers.zcc.scm.common.vo.AuthorizationVO;

/**
 * The Class LoginFilter.
 *
 * @author zhangchangchun
 * @since 2021年4月22日
 */
public class LoginFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);

    private static List<String> urlWhiteList = new ArrayList<String>(1);

    private static long sessionValidateTime;

    private IUserDao userDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String urlWhitelistStr = filterConfig.getInitParameter("urlWhiteList");
        if (!StringUtils.isEmpty(urlWhitelistStr)) {
            urlWhiteList = Arrays.asList(urlWhitelistStr.split(";"));
        }
        userDao = ApplicationContextManager.getBean(IUserDao.class);
        sessionValidateTime = Long.parseLong(EnvironmentProps.getApplicationProp("application.session.validatetime"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String path = httpServletRequest.getRequestURI();
        LOGGER.debug(path);
        if (!urlWhiteList.isEmpty()) {
            for (String url : urlWhiteList) {
                if (path.contains(url)) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }
        if (path.contains("/publicservices/")) {
            String appId = httpServletRequest.getHeader("appId");
            if (StringUtils.isEmpty(appId)) {
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setStatus(401);
                httpServletResponse.getOutputStream()
                        .write("{\"code\":\"401\",\"message\":\"未携带应用Id\"}".getBytes("utf-8"));
                return;
            }
            if (UserCache.getUser() == null) {
                UserVO queryParam = new UserVO();
                queryParam.setUserName(appId);
                queryParam.setUserType(UserTypeEnum.VIRTUAL.getValue());
                UserVO user = userDao.findUser(queryParam);
                if (user == null) {
                    httpServletResponse.setContentType("application/json");
                    httpServletResponse.setStatus(401);
                    httpServletResponse.getOutputStream()
                            .write("{\"code\":\"401\",\"message\":\"应用未授权，请联系管理员授权\"}".getBytes("utf-8"));
                    return;
                }
                UserCache.setUser(user);
            }
        } else {
            String token = httpServletRequest.getHeader("Authorization");
            boolean pass = false;
            AuthorizationVO authVO = AuthorizationUtil.decryptToken(token);
            if (authVO != null && authVO.getUser() != null
                    && (System.currentTimeMillis() - authVO.getCreatedTime()) < sessionValidateTime) {
                pass = true;
            }
            if (!pass) {
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setStatus(403);
                httpServletResponse.getOutputStream()
                        .write("{\"code\":\"403\",\"message\":\"您当前未登录或登录已失效，请登录后继续操作\"}".getBytes("utf-8"));
                return;
            } else {
                UserCache.setUser(authVO.getUser());//redis服务器
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
