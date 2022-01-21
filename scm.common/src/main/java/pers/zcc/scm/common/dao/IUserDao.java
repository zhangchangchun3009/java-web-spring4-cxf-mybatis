package pers.zcc.scm.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import pers.zcc.scm.common.user.vo.ResourceVO;
import pers.zcc.scm.common.user.vo.UserVO;

public interface IUserDao {

    UserVO findUser(@Param("entity") UserVO queryParam);

    int hasPrivilege(@Param("user") UserVO user, @Param("resource") ResourceVO param);

    int generateUserId();

    void insert(@Param("user") UserVO user);

    List<UserVO> getPagedUserList(@Param("entity") UserVO param);

    void delete(@Param("entity") UserVO param);

}
