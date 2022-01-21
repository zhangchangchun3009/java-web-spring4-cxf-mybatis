
package pers.zcc.scm.common.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.github.pagehelper.PageInfo;

import pers.zcc.scm.common.vo.BatchVO;
import pers.zcc.scm.common.vo.ExcelErrorVO;
import pers.zcc.scm.common.vo.PageVO;
import pers.zcc.scm.common.vo.Response;

/**
 * Excel错误处理服务
 *
 * @author zhangchangchun
 * @since 2022年1月4日
 */
@Produces("application/json")
@Path("/excelErrorService")
public interface IExcelErrorService {

    @Path("/getList/page/{pageSize}/{pageNum}")
    @GET
    PageInfo<ExcelErrorVO> getList(@QueryParam("") ExcelErrorVO param, @PathParam("") PageVO pageVo);

    @POST
    @Path("/batch")
    Response<ExcelErrorVO> batch(BatchVO<ExcelErrorVO> batchVO);
}
