
package pers.zcc.scm.common.privilege;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 项目资源注解.（标记在service实现类上）
 *
 * @author zhangchangchun
 * @since 2021年4月2日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Resource {
    /**
     * Code.资源编码
     *
     * @return the string
     */
    String code();

    /**
     * Desc.资源描述
     *
     * @return the string
     */
    String desc();
}
