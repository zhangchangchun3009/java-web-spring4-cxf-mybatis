
package pers.zcc.scm.common.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pers.zcc.scm.common.constant.Constants;
import pers.zcc.scm.common.dao.ISequenceDao;
import pers.zcc.scm.common.privilege.Privilege;
import pers.zcc.scm.common.privilege.Resource;
import pers.zcc.scm.common.service.ICommonService;
import pers.zcc.scm.common.util.FileUtil;
import pers.zcc.scm.common.vo.Response;

/**
 * 公共服务
 * 
 * @author zhangchangchun
 * @since 2021年5月13日
 */
@Named
@Resource(code = "commonService", desc = "系统管理 公共服务")
public class CommonService implements ICommonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);

    /** The sequence dao. */
    @Inject
    private ISequenceDao sequenceDao;

    @Override
    @Privilege(code = Constants.PRIVILEGE_QUERY_CODE, desc = Constants.PRIVILEGE_QUERY_DESC)
    public Response<Long> getSequenceValue() {
        Response<Long> response = new Response<Long>();
        long data = 0L;
        try {
            data = sequenceDao.getNextValue("s_common");
        } catch (Exception e) {
            LOGGER.error("getSequenceValue Exception,", e);
            return response.fail("001", e.getMessage());
        }
        return response.success(data);
    }

    @Override
    public Response<Long> getSequenceValueByName(String sequenceName) {
        Response<Long> response = new Response<Long>();
        if (sequenceName == null || sequenceName.equals("")) {
            return response.fail("001", "序列名不能为空");
        }
        long data = 0L;
        try {
            data = sequenceDao.getNextValue(sequenceName);
        } catch (Exception e) {
            LOGGER.error("getSequenceValueByName Exception,", e);
            return response.fail("002", "序列名不存在");
        }
        return response.success(data);
    }

    @Override
    public void getTaskFile(String taskid, String fileName, HttpServletResponse response) throws IOException {
        File tempDir = new File(SystemUtils.JAVA_IO_TMPDIR + File.separator + taskid);
        File tempFile = new File(tempDir, fileName);

        try {
            response.setContentType(FileUtil.getMimeType(fileName));
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            OutputStream os = response.getOutputStream();
            FileUtils.copyFile(tempFile, os);
            FileUtils.deleteQuietly(tempFile);
            FileUtils.deleteDirectory(tempDir);
        } catch (IOException e) {
            LOGGER.error("commonService getTaskFile failed, ", e);
            response.reset();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
