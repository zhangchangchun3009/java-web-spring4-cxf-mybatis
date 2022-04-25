
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
import pers.zcc.scm.common.user.service.interfaces.IUserRoleService;
import pers.zcc.scm.common.user.vo.RoleVO;
import pers.zcc.scm.common.user.vo.UserVO;
import pers.zcc.scm.common.vo.BatchVO;
import pers.zcc.scm.common.vo.Response;

/**
 * 用户角色管理.
 *
 * @author zhangchangchun
 * @since 2021年4月13日
 */
@Named
@Resource(code = "userRoleService", desc = "系统管理 用户角色管理")
public class UserRoleService implements IUserRoleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleService.class);

    @Inject
    private IPrivilegeDao privilegeDao;

    @Override
    @Privilege(code = Constants.PRIVILEGE_QUERY_CODE, desc = Constants.PRIVILEGE_QUERY_DESC)
    public Response<List<RoleVO>> getRoleListByUser(UserVO user) {
        Response<List<RoleVO>> response = new Response<List<RoleVO>>().success();
        if (user == null || user.getUserId() <= 0) {
            return response.fail("002", "用户不能为空");
        }
        List<RoleVO> roleList = null;
        try {
            roleList = privilegeDao.getRoleListByUser(user);
        } catch (Exception e) {
            LOGGER.error("getRoleListByUser exception:", e);
            return response.fail("001", "数据库异常");
        }
        return response.success(roleList);
    }

    @Override
    @Privilege(code = Constants.PRIVILEGE_INSERT_CODE, desc = Constants.PRIVILEGE_INSERT_DESC)
    public Response<String> insertRoleByUser(BatchVO<RoleVO> roles, Integer userId) {
        Response<String> response = new Response<String>().success();
        List<RoleVO> roleList = roles.getItemsToCreate();
        if (CollectionUtils.isEmpty(roleList) || userId == null) {
            return response.fail("002", "用户id和新增角色不能为空");
        }
        try {
            privilegeDao.insertRoleByUser(roleList, userId);
        } catch (Exception e) {
            LOGGER.error("insertRoleByUser exception:", e);
            return response.fail("001", "数据库异常");
        }
        return response;
    }

    @Override
    @Privilege(code = Constants.PRIVILEGE_DELETE_CODE, desc = Constants.PRIVILEGE_DELETE_DESC)
    public Response<String> deleteRoleByUser(BatchVO<RoleVO> roles, Integer userId) {
        Response<String> response = new Response<String>().success();
        List<RoleVO> roleList = roles.getItemsToDelete();
        if (CollectionUtils.isEmpty(roleList) || userId == null) {
            return response.fail("002", "用户id和角色不能为空");
        }
        try {
            privilegeDao.deleteRoleByUser(roleList, userId);
        } catch (Exception e) {
            LOGGER.error("deleteRoleByUser exception:", e);
            return response.fail("001", "数据库异常");
        }
        return response;
    }

}
