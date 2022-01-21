
package pers.zcc.scm.common.frame;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pers.zcc.scm.common.privilege.NoPrivilegeException;
import pers.zcc.scm.common.privilege.SignatureVerifyException;

/**
 * The Class GlobalExceptionHandle.
 *
 * @author zhangchangchun
 * @since 2021年4月12日
 */
@Named
public class GlobalExceptionHandle implements ExceptionMapper<Exception> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @Override
    public Response toResponse(Exception exception) {
        LOGGER.error("GlobalExceptionHandle catch exception:", exception);
        ResponseBuilder builder = null;
        Map<String, Object> entity = new HashMap<String, Object>();
        if (exception instanceof NoPrivilegeException) {
            builder = Response.status(Response.Status.UNAUTHORIZED);
            entity.put("code", "401");
            entity.put("message", "您还没有该资源的访问权限");
        } else if (exception instanceof SignatureVerifyException) {
            builder = Response.status(Response.Status.UNAUTHORIZED);
            entity.put("code", "401");
            entity.put("message", "签名验证失败");
        } else {
            LOGGER.error("GlobalExceptionHandle catch exception:", exception);
            builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            entity.put("code", "500");
            entity.put("message", "服务器内部错误，请联系开发人员解决。错误概要：" + exception.getMessage());
        }
        builder.type("application/json; charset=UTF-8");
        builder.entity(entity);
        builder.language(Locale.SIMPLIFIED_CHINESE);
        Response response = builder.build();
        return response;
    }

}
