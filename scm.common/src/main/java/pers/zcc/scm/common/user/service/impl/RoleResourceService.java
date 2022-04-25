package pers.zcc.scm.common.user.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import pers.zcc.scm.common.constant.Constants;
import pers.zcc.scm.common.dao.IPrivilegeDao;
import pers.zcc.scm.common.frame.privillege.Privilege;
import pers.zcc.scm.common.frame.privillege.Resource;
import pers.zcc.scm.common.user.service.interfaces.IRoleResourceService;
import pers.zcc.scm.common.user.vo.ResourceVO;
import pers.zcc.scm.common.user.vo.RoleVO;
import pers.zcc.scm.common.vo.BatchVO;
import pers.zcc.scm.common.vo.Response;

@Named
@Resource(code = "roleResourceService", desc = "系统管理  角色资源管理")
public class RoleResourceService implements IRoleResourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleResourceService.class);

    @Inject
    private IPrivilegeDao privilegeDao;

    @Override
    @Privilege(code = Constants.PRIVILEGE_QUERY_CODE, desc = Constants.PRIVILEGE_QUERY_DESC)
    public Response<List<ResourceVO>> getResourceListByRole(RoleVO role) {
        Response<List<ResourceVO>> response = new Response<List<ResourceVO>>().success();
        if (role == null || role.getRoleId() <= 0) {
            return response.fail("002", "角色不能为空");
        }
        List<ResourceVO> resourceList = null;
        try {
            resourceList = privilegeDao.getResourceListByRole(role);
        } catch (Exception e) {
            LOGGER.error("getResourceListByRole exception:", e);
            return response.fail("001", "数据库异常");
        }
        return response.success(resourceList);
    }

    @Override
    @Privilege(code = Constants.PRIVILEGE_INSERT_CODE, desc = Constants.PRIVILEGE_INSERT_DESC)
    public Response<String> insertResourceByRole(BatchVO<ResourceVO> resources, Integer roleId) {
        Response<String> response = new Response<String>().success();
        List<ResourceVO> resourceList = resources.getItemsToCreate();
        if (CollectionUtils.isEmpty(resourceList) || roleId == null) {
            return response.fail("002", "角色id和新增资源不能为空");
        }
        try {
            privilegeDao.insertResourceByRole(resourceList, roleId);
        } catch (Exception e) {
            LOGGER.error("insertResourceByRole exception:", e);
            return response.fail("001", "数据库异常");
        }
        return response;
    }

    @Override
    @Privilege(code = Constants.PRIVILEGE_DELETE_CODE, desc = Constants.PRIVILEGE_DELETE_DESC)
    public Response<String> deleteResourceByRole(BatchVO<ResourceVO> resources, Integer roleId) {
        Response<String> response = new Response<String>().success();
        List<ResourceVO> resourceList = resources.getItemsToDelete();
        if (CollectionUtils.isEmpty(resourceList) || roleId == null) {
            return response.fail("002", "角色id和待删除资源不能为空");
        }
        try {
            privilegeDao.deleteResourceByRole(resourceList, roleId);
        } catch (Exception e) {
            LOGGER.error("deleteResourceByRole exception:", e);
            return response.fail("001", "数据库异常");
        }
        return response;
    }

    @Override
    @Privilege(code = Constants.PRIVILEGE_BATCH_CODE, desc = Constants.PRIVILEGE_BATCH_DESC)
    public Response<String> saveResourceByRole(List<ResourceVO> resources, Integer roleId) {
        Response<String> response = new Response<String>().success();
        try {
            privilegeDao.deleteAllResourceByRole(roleId);
            privilegeDao.insertResourceByRole(resources, roleId);
        } catch (Exception e) {
            LOGGER.error("saveResourceByRole exception:", e);
            return response.fail("001", "数据库异常");
        }
        return response;
    }

}
