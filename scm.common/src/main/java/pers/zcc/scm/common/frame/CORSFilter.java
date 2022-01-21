
package pers.zcc.scm.common.frame;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

/**
 * The Class CORSFilter.
 */
/**
 * @author zhangchangchun
 * @since 2021年7月8日
 */
public class CORSFilter implements Filter {
    private static String origin;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        origin = filterConfig.getInitParameter("origin");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String requestOrigin = httpServletRequest.getHeader("origin");
        if (requestOrigin != null && requestOrigin != "") {
            origin = requestOrigin;
        }
        httpServletResponse.addHeader("Access-Control-Allow-Origin",
                origin != null ? origin : httpServletRequest.getRemoteHost());
        httpServletResponse.addHeader("Access-Control-Allow-Headers",
                "Accept, Authorization,DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type");
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.addHeader("Access-Control-Max-Age", "600");
        final Collection<String> cookiesHeaders = httpServletResponse.getHeaders(HttpHeaders.SET_COOKIE);
        boolean firstHeader = true;
        for (final String cookieHeader : cookiesHeaders) {
            if (StringUtils.isEmpty(cookieHeader)) {
                continue;
            }
            String customizedCookieHeader = "";
            StringBuilder sb = new StringBuilder();
            customizedCookieHeader = sb.append(cookieHeader).append(";SameSite=None;Secure").toString();
            if (firstHeader) {
                httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, customizedCookieHeader);
                firstHeader = false;
            } else {
                httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, customizedCookieHeader);
            }
        }
        String method = ((HttpServletRequest) request).getMethod();
        if (method.equals("OPTIONS")) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        } else {
            chain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    public void destroy() {

    }

}
