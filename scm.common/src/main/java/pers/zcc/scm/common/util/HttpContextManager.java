
package pers.zcc.scm.common.util;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import pers.zcc.scm.common.user.vo.UserVO;

/**
 * The Class HttpContextManager.
 *
 * @author zhangchangchun
 * @since 2021年4月6日
 */
public class HttpContextManager {

    /**
     * Gets the user.
     *
     * @return the user
     */
    public static UserVO getUser() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        HttpSession session = request.getSession();
        return session == null ? null : (UserVO) session.getAttribute("user");
    }

    /**
     * Gets the HttpServletRequest.
     *
     * @return the request
     */
    public static HttpServletRequest getRequest() {
        Message message = PhaseInterceptorChain.getCurrentMessage();
        if (message == null) {
            return null;
        }
        return (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
    }

    /**
     * Gets the HttpServletResponse.
     *
     * @return the response
     */
    public static HttpServletResponse getResponse() {
        Message message = PhaseInterceptorChain.getCurrentMessage();
        if (message == null) {
            return null;
        }
        return (HttpServletResponse) message.get(AbstractHTTPDestination.HTTP_RESPONSE);
    }

    /**
     * Gets the client ip.
     *
     * @param request the request
     * @return the client ip
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    /**
     * Gets the request body.
     *
     * @param req the req
     * @return the request body
     */
    public static String getRequestBody(HttpServletRequest req) {
        try {
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder(64);
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
