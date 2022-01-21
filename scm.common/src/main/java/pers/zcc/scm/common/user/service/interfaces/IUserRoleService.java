
package pers.zcc.scm.common.user.service.interfaces;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import pers.zcc.scm.common.user.vo.RoleVO;
import pers.zcc.scm.common.user.vo.UserVO;
import pers.zcc.scm.common.vo.BatchVO;
import pers.zcc.scm.common.vo.Response;

/**
 * The Class IUserRoleService.
 *
 * @author zhangchangchun
 * @since 2021年4月12日
 */
@Produces("application/json")
@Path("/userRoleService")
public interface IUserRoleService {

    /**
     * 根据用户id查找用户角色列表.
     *
     * @param user the user
     * @return the role list by user
     */
    @Path("/getRoleListByUser")
    @GET
    Response<List<RoleVO>> getRoleListByUser(@QueryParam("") UserVO user);

    /**
     * 给用户增加角色.
     *
     * @param roles the roles
     * @param userId the user id
     * @return the response
     */
    @Path("/insertRoleByUser/{userId}")
    @POST
    Response<String> insertRoleByUser(BatchVO<RoleVO> roles, @PathParam("userId") Integer userId);

    /**
     * 给用户删除角色.
     *
     * @param roles the roles
     * @param userId the user id
     * @return the response
     */
    @Path("/deleteRoleByUser/{userId}")
    @POST
    Response<String> deleteRoleByUser(BatchVO<RoleVO> roles, @PathParam("userId") Integer userId);

}
