
package pers.zcc.scm.common.user.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;

import pers.zcc.scm.common.constant.Constants;
import pers.zcc.scm.common.dao.IPrivilegeDao;
import pers.zcc.scm.common.privilege.Privilege;
import pers.zcc.scm.common.privilege.Resource;
import pers.zcc.scm.common.user.service.interfaces.IResourceService;
import pers.zcc.scm.common.user.vo.ResourceVO;
import pers.zcc.scm.common.util.ApplicationContextManager;
import pers.zcc.scm.common.vo.Response;

/**
 * 资源管理.
 *
 * @author zhangchangchun
 * @since 2021年4月13日
 */
@Named
@Resource(code = "resourceService", desc = "系统管理  资源/权限管理")
public class ResourceService implements IResourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);

    @Inject
    private IPrivilegeDao privilegeDao;

    @Override
    @Privilege(code = Constants.PRIVILEGE_QUERY_CODE, desc = Constants.PRIVILEGE_QUERY_DESC)
    public Response<List<ResourceVO>> queryResourceList() {
        Response<List<ResourceVO>> response = new Response<List<ResourceVO>>().success();
        List<ResourceVO> resourceList = null;
        try {
            resourceList = privilegeDao.queryResourceList();
        } catch (Exception e) {
            LOGGER.error("queryResourceList exception:", e);
            return response.fail("001", "数据库异常");
        }
        return response.success(resourceList);
    }

    @Override
    @Privilege(code = "scan", desc = "扫描项目资源")
    public void scan() {
        WebApplicationContext context = ApplicationContextManager.getApplicationContext();
        Map<String, Object> beanMap = context.getBeansWithAnnotation(Resource.class);
        List<ResourceVO> resourceList = new ArrayList<>(10);
        for (Object bean : beanMap.values()) {
            Class<? extends Object> ocl = bean.getClass();
            Class<?> cl = ClassUtils.getUserClass(ocl);
            LOGGER.debug(cl.getName());
            Resource resource = cl.getDeclaredAnnotation(Resource.class);
            if (resource == null) {
                continue;
            }
            String resourceCode = resource.code();
            String resourceDesc = resource.desc();
            String[] rda = resourceDesc.split(" ");
            String moduelName = "";
            String serviceName = "";
            if (rda.length == 1) {
                serviceName = rda[0];
            } else {
                moduelName = rda[0];
                serviceName = rda[rda.length - 1];
            }
            LOGGER.debug("code:" + resourceCode + ",desc:" + resourceDesc);
            Method[] methods = cl.getDeclaredMethods();
            if (methods == null) {
                continue;
            }
            for (Method method : methods) {
                Privilege pl = method.getDeclaredAnnotation(Privilege.class);
                if (pl == null) {
                    continue;
                }
                String privilegeCode = pl.code();
                String privilegeDesc = pl.desc();
                ResourceVO resourceVO = new ResourceVO();
                resourceVO.setModuleName(moduelName);
                resourceVO.setServiceCode(resourceCode);
                resourceVO.setServiceName(serviceName);
                resourceVO.setMethodCode(privilegeCode);
                resourceVO.setMethodName(privilegeDesc);
                resourceList.add(resourceVO);
            }
            if (resourceList.size() > 0) {
                privilegeDao.batchInsert(resourceList);
                resourceList.clear();
            }
        }
        privilegeDao.deleteAllInvalid();
    }

}
