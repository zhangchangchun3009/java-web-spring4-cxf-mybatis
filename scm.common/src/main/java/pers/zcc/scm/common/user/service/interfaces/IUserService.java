
package pers.zcc.scm.common.user.service.interfaces;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.github.pagehelper.PageInfo;

import pers.zcc.scm.common.user.vo.UserVO;
import pers.zcc.scm.common.vo.PageVO;
import pers.zcc.scm.common.vo.Response;

/**
 * The Interface IUserService.
 *
 * @author zhangchangchun
 * @since 2021年4月6日
 */
@Path("/user")
@Produces("application/json")
public interface IUserService {

    UserVO findUser(UserVO userId);

    /**
     * Gets the paged user list.
     *
     * @param param the param
     * @param pageVO the page VO
     * @return the paged user list
     */
    @GET
    @Path("/getPagedUserList/page/{pageSize}/{pageNum}")
    PageInfo<UserVO> getPagedUserList(@QueryParam("") UserVO param, @PathParam("") PageVO pageVO);

    /**
     * Adds the user.
     *
     * @param param the param
     * @return the response
     */
    @POST
    @Path("/addUser")
    Response<String> addUser(UserVO param);

    /**
     * Delete user.
     *
     * @param param the param
     * @return the response
     */
    @POST
    @Path("/deleteUser")
    Response<String> deleteUser(UserVO param);

    /**
     * Regist.
     *
     * @param request the request
     * @param response the response
     * @param param the param
     * @return the response
     */
    @POST
    @Path("/regist")
    Response<String> regist(@Context HttpServletRequest request, @Context HttpServletResponse response, UserVO param);

    /**
     * Login.
     *
     * @param request the request
     * @param response the response
     * @param param the param
     * @return the response
     */
    @POST
    @Path("/login")
    Response<String> login(@Context HttpServletRequest request, @Context HttpServletResponse response, UserVO param);

    /**
     * Login by token.
     *
     * @param request  the request
     * @param response the response
     * @param param    the param
     * @return the response
     */
    @POST
    @Path("/login/byToken")
    Response<Map<String, Object>> loginByToken(@Context HttpServletRequest request,
            @Context HttpServletResponse response, UserVO param);

    /**
     * Logout.
     *
     * @param request the request
     * @param response the response
     * @param param the param
     * @return the response
     */
    @POST
    @Path("/logout")
    Response<String> logout(@Context HttpServletRequest request, @Context HttpServletResponse response, UserVO param);
}
