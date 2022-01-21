
package pers.zcc.scm.common.privilege;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解
 * 
 * @author zhangchangchun
 * @since 2021年4月2日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Privilege {

    /**
     * Code.权限编码
     *
     * @return the string
     */
    String code();

    /**
     * Desc.权限描述
     *
     * @return the string
     */
    String desc();

}
