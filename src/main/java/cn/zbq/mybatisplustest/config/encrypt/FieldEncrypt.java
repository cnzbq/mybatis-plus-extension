package cn.zbq.mybatisplustest.config.encrypt;

import java.lang.annotation.*;

/**
 * @author zbq
 * @since 2022/11/20
 */
@Documented
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldEncrypt {
}
