
package pers.zcc.scm.common.user.service.interfaces;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import pers.zcc.scm.common.user.vo.ResourceVO;
import pers.zcc.scm.common.vo.Response;

/**
 * 资源管理
 *
 * @author zhangchangchun
 * @since 2021年4月13日
 */
@Produces("application/json")
@Path("/resourceService")
public interface IResourceService {

    /**
     * 查询所有资源列表.
     *
     * @return the response
     */
    @Path("/queryResourceList")
    @GET
    Response<List<ResourceVO>> queryResourceList();

    /**
     * 扫描更新项目资源
     */
    @Path("/scan")
    @GET
    void scan();

}
