package pers.zcc.scm.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import pers.zcc.scm.common.constant.AsyncTaskStatusEnum;
import pers.zcc.scm.common.constant.Constants;
import pers.zcc.scm.common.dao.IAsyncTaskEventResultDao;
import pers.zcc.scm.common.privilege.Privilege;
import pers.zcc.scm.common.privilege.Resource;
import pers.zcc.scm.common.service.IAsyncTaskEventResultService;
import pers.zcc.scm.common.vo.AsyncTaskEventResultVO;
import pers.zcc.scm.common.vo.BatchVO;
import pers.zcc.scm.common.vo.PageVO;
import pers.zcc.scm.common.vo.Response;

@Named
@Resource(code = "asyncTaskEventResultService", desc = "系统管理  异步任务结果查询")
public class AsyncTaskEventResultService implements IAsyncTaskEventResultService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTaskEventResultService.class);

    @Inject
    private IAsyncTaskEventResultDao asyncTaskEventResultDao;

    @Override
    @Privilege(code = Constants.PRIVILEGE_QUERY_CODE, desc = Constants.PRIVILEGE_QUERY_DESC)
    public PageInfo<AsyncTaskEventResultVO> getList(AsyncTaskEventResultVO param, PageVO pageVo) {
        PageHelper.startPage(pageVo.getPageNum(), pageVo.getPageSize());
        List<AsyncTaskEventResultVO> list = asyncTaskEventResultDao.getList(param);
        PageInfo<AsyncTaskEventResultVO> result = new PageInfo<>(list);
        return result;
    }

    @Override
    @Privilege(code = Constants.PRIVILEGE_BATCH_CODE, desc = Constants.PRIVILEGE_BATCH_DESC)
    public Response<AsyncTaskEventResultVO> batch(BatchVO<AsyncTaskEventResultVO> batchVO) {
        Response<AsyncTaskEventResultVO> response = new Response<AsyncTaskEventResultVO>().success();
        if (batchVO == null) {
            return response;
        }

        List<AsyncTaskEventResultVO> itemsToCreate = batchVO.getItemsToCreate();
        List<AsyncTaskEventResultVO> itemsToUpdate = batchVO.getItemsToUpdate();
        List<AsyncTaskEventResultVO> itemsToDelete = batchVO.getItemsToDelete();
        if (!CollectionUtils.isEmpty(itemsToCreate)) {
            try {
                asyncTaskEventResultDao.insert(itemsToCreate);
            } catch (Exception e) {
                LOGGER.error("batch insert exception:", e);
                return response.fail("001", "数据库插入数据出错");
            }
        }
        if (!CollectionUtils.isEmpty(itemsToUpdate)) {
            try {
                asyncTaskEventResultDao.update(itemsToUpdate);
            } catch (Exception e) {
                LOGGER.error("batch update exception:", e);
                return response.fail("002", "数据库修改数据出错");
            }
        }
        if (!CollectionUtils.isEmpty(itemsToDelete)) {
            try {
                asyncTaskEventResultDao.delete(itemsToDelete);
            } catch (Exception e) {
                LOGGER.error("batch delete exception:", e);
                return response.fail("003", "数据库删除数据出错");
            }
        }
        return response;

    }

    @Override
    public void updateAsyncTaskStatus(Long eventId, AsyncTaskStatusEnum status) {
        BatchVO<AsyncTaskEventResultVO> batchVO = new BatchVO<>();
        List<AsyncTaskEventResultVO> itemsToUpdate = new ArrayList<>();
        AsyncTaskEventResultVO task = new AsyncTaskEventResultVO();
        task.setId(eventId);
        task.setStatus(status.getValue());
        if (status == AsyncTaskStatusEnum.COMPLETED || status == AsyncTaskStatusEnum.STOPPED) {
            task.setEndTime(System.currentTimeMillis());
        }
        itemsToUpdate.add(task);
        batchVO.setItemsToUpdate(itemsToUpdate);
        batch(batchVO);
    }

}
