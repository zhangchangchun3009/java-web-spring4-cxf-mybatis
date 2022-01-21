
package pers.zcc.scm.common.user.service.interfaces;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import pers.zcc.scm.common.user.vo.RoleVO;
import pers.zcc.scm.common.vo.BatchVO;
import pers.zcc.scm.common.vo.Response;

/**
 * 角色管理.
 *
 * @author zhangchangchun
 * @since 2021年4月13日
 */
@Produces("application/json")
@Path("/roleService")
public interface IRoleService {

    /**
     * （根据角色名）查找角色列表.
     *
     * @param role the role
     * @return the role list
     */
    @Path("/getRoleList")
    @GET
    Response<List<RoleVO>> getRoleList(@QueryParam("") RoleVO role);

    /**
     * 增加角色.
     *
     * @param roles the roles
     * @return the response
     */
    @Path("/insertRole")
    @POST
    Response<String> insertRole(BatchVO<RoleVO> roles);

    /**
     * 删除角色.
     *
     * @param roles the roles
     * @return the response
     */
    @Path("/deleteRole")
    @POST
    Response<String> deleteRole(BatchVO<RoleVO> roles);

}
