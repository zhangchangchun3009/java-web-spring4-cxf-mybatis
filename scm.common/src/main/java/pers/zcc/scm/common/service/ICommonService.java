
package pers.zcc.scm.common.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import pers.zcc.scm.common.vo.Response;

/**
 * 公共接口服务
 * 
 * @author zhangchangchun
 * @since 2021年5月13日
 */
@Produces("application/json")
@Path("/commonService")
public interface ICommonService {

    /**
     * Gets the sequence value.
     *
     * @return the sequence value by name
     */
    @GET
    @Path("/getSequenceValue")
    Response<Long> getSequenceValue();

    Response<Long> getSequenceValueByName(String sequenceName);

    @GET
    @Path("/getTaskFile/{taskid}/{fileName}")
    void getTaskFile(@PathParam("taskid") String taskid, @PathParam("fileName") String fileName,
            @Context HttpServletResponse response) throws IOException;
}
