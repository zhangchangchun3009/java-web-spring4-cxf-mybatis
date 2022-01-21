
package pers.zcc.scm.common.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.github.pagehelper.PageInfo;

import pers.zcc.scm.common.constant.AsyncTaskStatusEnum;
import pers.zcc.scm.common.vo.AsyncTaskEventResultVO;
import pers.zcc.scm.common.vo.BatchVO;
import pers.zcc.scm.common.vo.PageVO;
import pers.zcc.scm.common.vo.Response;

/**
 * 异步任务结果服务
 *
 * @author zhangchangchun
 * @since 2022年1月4日
 */
@Produces("application/json")
@Path("/asyncTaskEventResultService")
public interface IAsyncTaskEventResultService {

    @Path("/getList/page/{pageSize}/{pageNum}")
    @GET
    PageInfo<AsyncTaskEventResultVO> getList(@QueryParam("") AsyncTaskEventResultVO param,
            @PathParam("") PageVO pageVo);

    @POST
    @Path("/batch")
    Response<AsyncTaskEventResultVO> batch(BatchVO<AsyncTaskEventResultVO> batchVO);

    void updateAsyncTaskStatus(Long eventId, AsyncTaskStatusEnum status);

}
