package pers.zcc.scm.common.user.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import pers.zcc.scm.common.constant.Constants;
import pers.zcc.scm.common.dao.IPrivilegeDao;
import pers.zcc.scm.common.privilege.Privilege;
import pers.zcc.scm.common.privilege.Resource;
import pers.zcc.scm.common.user.service.interfaces.IRoleService;
import pers.zcc.scm.common.user.vo.RoleVO;
import pers.zcc.scm.common.vo.BatchVO;
import pers.zcc.scm.common.vo.Response;

@Named
@Resource(code = "roleService", desc = "系统管理  角色管理")
public class RoleService implements IRoleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

    @Inject
    private IPrivilegeDao privilegeDao;

    @Override
    @Privilege(code = Constants.PRIVILEGE_QUERY_CODE, desc = Constants.PRIVILEGE_QUERY_DESC)
    public Response<List<RoleVO>> getRoleList(RoleVO role) {
        Response<List<RoleVO>> response = new Response<List<RoleVO>>().success();
        List<RoleVO> list = null;
        try {
            list = privilegeDao.getRoleList(role);
        } catch (Exception e) {
            LOGGER.error("getRoleList exception:", e);
            return response.fail("001", "数据库异常");
        }
        return response.success(list);
    }

    @Override
    @Privilege(code = Constants.PRIVILEGE_INSERT_CODE, desc = Constants.PRIVILEGE_INSERT_DESC)
    public Response<String> insertRole(BatchVO<RoleVO> roles) {
        Response<String> response = new Response<String>().success();
        List<RoleVO> roleList = roles.getItemsToCreate();
        if (CollectionUtils.isEmpty(roleList)) {
            return response;
        }
        try {
            privilegeDao.insertRole(roleList);
        } catch (Exception e) {
            LOGGER.error("insertRole exception:", e);
            return response.fail("001", "数据库异常");
        }
        return response;
    }

    @Override
    @Privilege(code = Constants.PRIVILEGE_DELETE_CODE, desc = Constants.PRIVILEGE_DELETE_DESC)
    public Response<String> deleteRole(BatchVO<RoleVO> roles) {
        Response<String> response = new Response<String>().success();
        List<RoleVO> roleList = roles.getItemsToDelete();
        if (CollectionUtils.isEmpty(roleList)) {
            return response;
        }
        try {
            privilegeDao.deleteRole(roleList);
        } catch (Exception e) {
            LOGGER.error("deleteRole exception:", e);
            return response.fail("001", "数据库异常");
        }
        return response;
    }

}
