package cn.zbq.mybatisplustest.demo.service.impl;

import cn.zbq.mybatisplustest.demo.service.DynamicDataSourceService;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author zbq
 * @since 2022/7/19
 */
@Slf4j
@Service
@DS("#root.target.dbType")
public class DynamicDataSourceServiceImpl implements DynamicDataSourceService {
    @Getter
    @Value("${app.db-type}")
    private String dbType;

    @Override
    public void test() {
        log.info("current db type:[{}]", this.dbType);
    }
}
