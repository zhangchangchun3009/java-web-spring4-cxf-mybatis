
package pers.zcc.scm.common.user.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import pers.zcc.scm.common.constant.UserTypeEnum;
import pers.zcc.scm.common.dao.IUserDao;
import pers.zcc.scm.common.frame.AuthorizationUtil;
import pers.zcc.scm.common.privilege.Privilege;
import pers.zcc.scm.common.privilege.Resource;
import pers.zcc.scm.common.user.service.interfaces.IUserService;
import pers.zcc.scm.common.user.vo.UserVO;
import pers.zcc.scm.common.vo.PageVO;
import pers.zcc.scm.common.vo.Response;

/**
 * The Class UserService.
 *
 * @author zhangchangchun
 * @since 2021年4月6日
 */
@Named
@Resource(code = "userService", desc = "系统管理 用户管理")
public class UserService implements IUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Value("${scm.common.user.encrypt.salt}")
    private String ENCRYPT_PREFIX;

    @Inject
    private IUserDao userDao;

    @Override
    public UserVO findUser(UserVO queryParam) {
        return userDao.findUser(queryParam);
    }

    @Override
    @Privilege(code = "getPagedUserList", desc = "分页查询所有用户信息")
    public PageInfo<UserVO> getPagedUserList(UserVO param, PageVO pageVO) {
        PageHelper.startPage(pageVO.getPageNum(), pageVO.getPageSize());
        List<UserVO> list = userDao.getPagedUserList(param);
        PageInfo<UserVO> result = new PageInfo<UserVO>(list);
        return result;
    }

    @Override
    public Response<String> regist(HttpServletRequest request, HttpServletResponse response, UserVO param) {
        Response<String> result = new Response<String>().success();
        if (param == null || StringUtils.isEmpty(param.getUserName()) || StringUtils.isEmpty(param.getPassword())) {
            return result.fail("001", "请输入信息");
        }
        String password = param.getPassword();
        String encPassword = null;
        try {
            encPassword = DigestUtils.md5DigestAsHex(
                    (ENCRYPT_PREFIX + DigestUtils.md5DigestAsHex(password.getBytes("utf-8"))).getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("login UnsupportedEncodingException", e);
        }
        UserVO user = new UserVO();
        user.setUserName(param.getUserName());
        try {
            UserVO userDb = userDao.findUser(param);
            if (userDb != null) {
                return result.fail("002", "用户名已存在！");
            }
            int userId = userDao.generateUserId();
            user.setUserId(userId);
            user.setPassword(encPassword);
            user.setUserType(UserTypeEnum.DEFAULT.getValue());
            userDao.insert(user);
        } catch (Exception e) {
            LOGGER.error("userDao.insert(user)", e);
            return result.fail("003", "数据库异常");
        }
        return result;
    }

    @Override
    @Privilege(code = "addUser", desc = "添加用户")
    public Response<String> addUser(UserVO param) {
        Response<String> result = new Response<String>().success();
        if (param == null || StringUtils.isEmpty(param.getUserName()) || StringUtils.isEmpty(param.getUserType())
                || (!UserTypeEnum.VIRTUAL.getValue().equalsIgnoreCase(param.getUserType())
                        && StringUtils.isEmpty(param.getPassword()))) {
            return result.fail("001", "请输入信息");
        }
        String password = param.getPassword();
        String encPassword = null;
        if (!StringUtils.isEmpty(password)) {
            try {
                encPassword = DigestUtils.md5DigestAsHex(
                        (ENCRYPT_PREFIX + DigestUtils.md5DigestAsHex(password.getBytes("utf-8"))).getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("login UnsupportedEncodingException", e);
            }
        }
        UserVO user = new UserVO();
        user.setUserName(param.getUserName());
        try {
            UserVO userDb = userDao.findUser(param);
            if (userDb != null) {
                return result.fail("002", "用户名已存在！");
            }
            int userId = userDao.generateUserId();
            user.setUserId(userId);
            user.setPassword(encPassword);
            user.setUserType(param.getUserType());
            userDao.insert(user);
        } catch (Exception e) {
            LOGGER.error("userDao.insert(user)", e);
            return result.fail("003", "数据库异常");
        }
        return result;
    }

    @Override
    @Privilege(code = "deleteUser", desc = "删除用户")
    public Response<String> deleteUser(UserVO param) {
        Response<String> result = new Response<String>().success();
        if (param == null || param.getId() == 0) {
            return result.fail("001", "请输入信息");
        }
        userDao.delete(param);
        return result;
    }

    @Override
    public Response<String> login(HttpServletRequest request, HttpServletResponse response, UserVO param) {
        Response<String> result = new Response<String>().success();
        if (param == null || StringUtils.isEmpty(param.getUserName())) {
            return result.fail("001", "请输入信息");
        }
        UserVO userDb = userDao.findUser(param);
        if (userDb == null) {
            return result.fail("002", "请先注册");
        }
        if ("guest".equalsIgnoreCase(userDb.getUserName())) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", userDb);
            return result;
        }
        if (StringUtils.isEmpty(param.getPassword())) {
            return result.fail("005", "请输入密码");
        }
        String password = param.getPassword();
        String encPassword = null;
        try {
            encPassword = DigestUtils.md5DigestAsHex(
                    (ENCRYPT_PREFIX + DigestUtils.md5DigestAsHex(password.getBytes("utf-8"))).getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("login UnsupportedEncodingException", e);
        }
        if (!param.getUserName().equals(userDb.getUserName())) {
            return result.fail("003", "用户不存在");
        }
        if (!encPassword.equals(userDb.getPassword())) {
            return result.fail("004", "密码不正确");
        }
        userDb.setPassword(null);
        HttpSession session = request.getSession(true);
        session.setAttribute("user", userDb);
        return result.success(session.getId());
    }

    @Override
    public Response<Map<String, Object>> loginByToken(HttpServletRequest request, HttpServletResponse response,
            UserVO param) {
        Map<String, Object> data = new HashMap<String, Object>(4);
        Response<Map<String, Object>> result = new Response<Map<String, Object>>().success(data);
        if (param == null || StringUtils.isEmpty(param.getUserName())) {
            return result.fail("001", "请输入信息");
        }
        UserVO userDb = userDao.findUser(param);
        if (userDb == null) {
            return result.fail("002", "请先注册");
        }
        if ("guest".equalsIgnoreCase(userDb.getUserName())) {
            userDb.setPassword(null);
            data.put("token", AuthorizationUtil.generateToken(userDb));
            data.put("user", userDb);
            return result;
        }
        if (StringUtils.isEmpty(param.getPassword())) {
            return result.fail("005", "请输入密码");
        }
        String password = param.getPassword();
        String encPassword = null;
        try {
            encPassword = DigestUtils.md5DigestAsHex(
                    (ENCRYPT_PREFIX + DigestUtils.md5DigestAsHex(password.getBytes("utf-8"))).getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("login UnsupportedEncodingException", e);
        }
        if (!param.getUserName().equals(userDb.getUserName())) {
            return result.fail("003", "用户不存在");
        }
        if (!encPassword.equals(userDb.getPassword())) {
            return result.fail("004", "密码不正确");
        }
        userDb.setPassword(null);
        data.put("token", AuthorizationUtil.generateToken(userDb));
        data.put("user", userDb);
        return result;
    }

    @Override
    public Response<String> logout(HttpServletRequest request, HttpServletResponse response, UserVO param) {
        Response<String> result = new Response<String>().success();
        request.getSession().invalidate();
        return result;
    }

}
