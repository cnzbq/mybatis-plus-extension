package cn.zbq.mybatisplustest.config.encrypt;

import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 解密拦截器
 *
 * @author zbq
 * @since 2022/11/20
 */
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
public class DecryptInterceptor implements Interceptor {
    private final String key;

    public DecryptInterceptor(String key) {
        this.key = key;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        List<?> result = (List<?>) invocation.proceed();
        if (result.isEmpty()) {
            return result;
        }

        Object target = invocation.getTarget();
        if (target instanceof DefaultResultSetHandler) {
            DefaultResultSetHandler resultSetHandler = (DefaultResultSetHandler) target;
            Field field = resultSetHandler.getClass().getDeclaredField("mappedStatement");
            field.setAccessible(true);
            MappedStatement ms = (MappedStatement) field.get(resultSetHandler);
            Configuration config = ms.getConfiguration();

            for (Object res : result) {
                if (Objects.nonNull(res) && !processFields(config, res)) {
                    break;
                }
            }

        }

        return result;
    }

    private boolean processFields(Configuration config, Object res) {
        Field[] fields = res.getClass().getDeclaredFields();
        Set<Field> decryptFields = getFiledSet(fields);

        if (Objects.isNull(decryptFields) || decryptFields.size() == 0) {
            return false;
        }

        MetaObject metaObject = config.newMetaObject(res);
        decryptFields.forEach(field -> {
            // 获取字段加密前的值
            Object originalVal = metaObject.getValue(field.getName());
            if (Objects.nonNull(originalVal)) {
                // 设置新的值
                metaObject.setValue(field.getName(), AESAlgorithm.decrypt((String) originalVal, key));
            }
        });

        return true;
    }

    private Set<Field> getFiledSet(Field[] fields) {
        return Arrays.stream(fields).filter(f -> f.isAnnotationPresent(FieldEncrypt.class) && f.getType() == String.class)
                .collect(Collectors.toSet());
    }

    @Override
    public Object plugin(Object target) {
        return target instanceof ResultSetHandler ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
