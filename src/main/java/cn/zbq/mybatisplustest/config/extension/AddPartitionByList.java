package cn.zbq.mybatisplustest.config.extension;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * MySql list添加分区
 * <p>https://dev.mysql.com/doc/refman/5.7/en/partitioning-list.html</p>
 *
 * @author zbq
 * @since 2022/7/3
 */
public class AddPartitionByList extends AbstractMethod {

    private static final String SQL_SCRIPT = "<script>\nALTER TABLE %s ADD PARTITION (PARTITION part_${v} VALUES IN (${v}))\n</script>";

    public AddPartitionByList() {
        super("addPartitionByList");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String tableName = tableInfo.getTableName();
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, String.format(SQL_SCRIPT, tableName), modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, sqlSource);
    }
}
