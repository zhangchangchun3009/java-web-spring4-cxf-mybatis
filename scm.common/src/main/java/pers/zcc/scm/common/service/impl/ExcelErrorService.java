package pers.zcc.scm.common.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import pers.zcc.scm.common.constant.Constants;
import pers.zcc.scm.common.dao.IExcelErrorDao;
import pers.zcc.scm.common.privilege.Privilege;
import pers.zcc.scm.common.privilege.Resource;
import pers.zcc.scm.common.service.IExcelErrorService;
import pers.zcc.scm.common.vo.BatchVO;
import pers.zcc.scm.common.vo.ExcelErrorVO;
import pers.zcc.scm.common.vo.PageVO;
import pers.zcc.scm.common.vo.Response;

@Named
@Resource(code = "excelErrorService", desc = "系统管理  excel处理错误信息查询")
public class ExcelErrorService implements IExcelErrorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelErrorService.class);

    @Inject
    private IExcelErrorDao excelErrorDao;

    @Override
    @Privilege(code = Constants.PRIVILEGE_QUERY_CODE, desc = Constants.PRIVILEGE_QUERY_DESC)
    public PageInfo<ExcelErrorVO> getList(ExcelErrorVO param, PageVO pageVo) {
        PageHelper.startPage(pageVo.getPageNum(), pageVo.getPageSize());
        List<ExcelErrorVO> list = excelErrorDao.getList(param);
        PageInfo<ExcelErrorVO> result = new PageInfo<>(list);
        return result;
    }

    @Override
    @Privilege(code = Constants.PRIVILEGE_BATCH_CODE, desc = Constants.PRIVILEGE_BATCH_DESC)
    public Response<ExcelErrorVO> batch(BatchVO<ExcelErrorVO> batchVO) {
        Response<ExcelErrorVO> response = new Response<ExcelErrorVO>().success();
        if (batchVO == null) {
            return response;
        }

        List<ExcelErrorVO> itemsToCreate = batchVO.getItemsToCreate();
        List<ExcelErrorVO> itemsToUpdate = batchVO.getItemsToUpdate();
        List<ExcelErrorVO> itemsToDelete = batchVO.getItemsToDelete();
        if (!CollectionUtils.isEmpty(itemsToCreate)) {
            try {
                excelErrorDao.insert(itemsToCreate);
            } catch (Exception e) {
                LOGGER.error("batch insert exception:", e);
                return response.fail("001", "数据库插入数据出错");
            }
        }
        if (!CollectionUtils.isEmpty(itemsToUpdate)) {
            try {
                excelErrorDao.update(itemsToUpdate);
            } catch (Exception e) {
                LOGGER.error("batch update exception:", e);
                return response.fail("002", "数据库修改数据出错");
            }
        }
        if (!CollectionUtils.isEmpty(itemsToDelete)) {
            try {
                excelErrorDao.delete(itemsToDelete);
            } catch (Exception e) {
                LOGGER.error("batch delete exception:", e);
                return response.fail("003", "数据库删除数据出错");
            }
        }
        return response;
    }

}
