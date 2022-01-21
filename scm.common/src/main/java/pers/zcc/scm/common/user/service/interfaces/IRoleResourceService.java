
package pers.zcc.scm.common.user.service.interfaces;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import pers.zcc.scm.common.user.vo.ResourceVO;
import pers.zcc.scm.common.user.vo.RoleVO;
import pers.zcc.scm.common.vo.BatchVO;
import pers.zcc.scm.common.vo.Response;

/**
 * 角色资源管理
 */
/**
 * @author zhangchangchun
 * @since 2021年4月13日
 */
@Produces("application/json")
@Path("/roleResourceService")
public interface IRoleResourceService {

    /**
     * 查找角色拥有的资源权限
     *
     * @param role the role
     * @return the resource list by role
     */
    @Path("/getResourceListByRole")
    @GET
    Response<List<ResourceVO>> getResourceListByRole(@QueryParam("") RoleVO role);

    /**
     * 给角色增加权限
     *
     * @param resources the resources
     * @param roleId the role id
     * @return the response
     */
    @Path("/insertResourceByRole/{roleId}")
    @POST
    Response<String> insertResourceByRole(BatchVO<ResourceVO> resources, @PathParam("roleId") Integer roleId);

    /**
     * 给角色删除权限
     *
     * @param resources the resources
     * @param roleId the role id
     * @return the response
     */
    @Path("/deleteResourceByRole/{roleId}")
    @POST
    Response<String> deleteResourceByRole(BatchVO<ResourceVO> resources, @PathParam("roleId") Integer roleId);

    /**
     * 修改角色权限后保存权限
     *
     * @param resources the resources
     * @param roleId the role id
     * @return the response
     */
    @Path("/saveResourceByRole/{roleId}")
    @POST
    Response<String> saveResourceByRole(List<ResourceVO> resources, @PathParam("roleId") Integer roleId);

}
