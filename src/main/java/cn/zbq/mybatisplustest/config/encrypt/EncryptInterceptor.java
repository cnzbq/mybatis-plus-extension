package cn.zbq.mybatisplustest.config.encrypt;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.ibatis.mapping.SqlCommandType.*;

/**
 * 加密拦截器
 *
 * @author zbq
 * @since 2022/11/20
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class EncryptInterceptor implements Interceptor {

    private final String key;

    public EncryptInterceptor(String key) {
        this.key = key;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        // 获取sql语句封装对象
        MappedStatement ms = (MappedStatement) args[0];
        // 获取方法执行的类型
        SqlCommandType sqlCommandType = ms.getSqlCommandType();
        // 只有插入和更新语句才需要进行加密
        if (sqlCommandType == INSERT || sqlCommandType == UPDATE) {
            Configuration config = ms.getConfiguration();
            Object params = args[1];
            if (params instanceof MapperMethod.ParamMap) {
                for (Map.Entry<String, ?> entry : ((MapperMethod.ParamMap<?>) params).entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    // 为空，忽略
                    if (Objects.isNull(value) || Objects.isNull(key)) {
                        continue;
                    }

                    if (value instanceof List) {
                        if (key.startsWith("param")) {
                            continue;
                        }

                        //  处理arrayList参数,处理之后要继续循环
                        //  处理加密问题
                        List<?> list = (List<?>) value;
                        for (Object obj : list) {
                            processValue(config, obj);
                        }

                        continue;
                    }
                    //  处理加密问题
                    processValue(config, value);
                }

            } else {
                //  处理加密问题
                processValue(config, params);
            }

        }

        // 继续执行
        return invocation.proceed();
    }

    /**
     * 处理值加密
     *
     * @param config /
     * @param value  /
     */
    private void processValue(Configuration config, Object value) {
        Field[] fields = value.getClass().getDeclaredFields();
        Set<Field> filedSet = getFiledSet(fields);
        processFields(config, filedSet, value);
    }

    private void processFields(Configuration config, Set<Field> encryptedFields, Object obj) {
        if (Objects.isNull(encryptedFields) || encryptedFields.size() == 0) {
            return;
        }

        MetaObject metaObject = config.newMetaObject(obj);
        encryptedFields.forEach(field -> {
            // 获取字段加密前的值
            Object originalVal = metaObject.getValue(field.getName());
            if (Objects.nonNull(originalVal)) {
                // 设置新的值
                metaObject.setValue(field.getName(), AESAlgorithm.encrypt((String) originalVal, key));
            }
        });

    }

    private Set<Field> getFiledSet(Field[] fields) {
        return Arrays.stream(fields).filter(f -> f.isAnnotationPresent(FieldEncrypt.class) && f.getType() == String.class)
                .collect(Collectors.toSet());
    }

    @Override
    public Object plugin(Object target) {
        return target instanceof Executor ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
