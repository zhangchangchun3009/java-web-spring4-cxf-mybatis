
package pers.zcc.scm.common.util;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import pers.zcc.scm.common.spring.event.AppCustomizedEvent;

/**
 * The Class ApplicationContextManager.
 *
 * @author zhangchangchun
 * @since 2021/3/29
 */
public class ApplicationContextManager {

    private ApplicationContextManager() {
    }

    private static final WebApplicationContext CONTEXT = ContextLoader.getCurrentWebApplicationContext();

    public static WebApplicationContext getApplicationContext() {
        return CONTEXT;
    }

    public static void publishEvent(AppCustomizedEvent<?> event) {
        CONTEXT.publishEvent(event);
    }

    public static Object getBean(String beanName) {
        return CONTEXT.getBean(beanName);
    }

    public static <T> T getBean(Class<T> beanClass) {
        return CONTEXT.getBean(beanClass);
    }

    public static <T> Collection<T> getBeanOfType(Class<T> type) {
        Map<String, T> beanMap = CONTEXT.getBeansOfType(type);
        if (beanMap != null && !beanMap.isEmpty()) {
            return beanMap.values();
        }
        return Collections.emptyList();
    }

    public static Collection<?> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        Map<String, Object> beanMap = CONTEXT.getBeansWithAnnotation(annotationType);
        if (beanMap != null && !beanMap.isEmpty()) {
            return beanMap.values();
        }
        return Collections.emptyList();
    }

}
