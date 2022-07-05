package cn.zbq.mybatisplustest.config.extension;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 查询分区信息list
 *
 * @author zbq
 * @since 2022/7/4
 */
public class SelectPartitionInfoList extends AbstractMethod {
    private static final String SQL_SCRIPT = "<script>\nselect %s from information_schema.partitions %s %s %s\n</script>";

    public SelectPartitionInfoList() {
        super("selectPartitionInfoList");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String tableName = tableInfo.getTableName();
        String resource = PartitionInfo.class.getName().replace(StringPool.DOT, StringPool.SLASH) + ".java (best guess)";
        MapperBuilderAssistant builderAssistant = new MapperBuilderAssistant(tableInfo.getConfiguration(), resource);
        TableInfo info = TableInfoHelper.initTableInfo(builderAssistant, PartitionInfo.class);
        String sqlSelect = info.getAllSqlSelect();

        String sql = String.format(SQL_SCRIPT, sqlSelect, sqlWhereEntityWrapper(info, tableName), sqlOrderBy(info), sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForOther(mapperClass, sqlSource, PartitionInfo.class);
    }

    private String sqlWhereEntityWrapper(TableInfo table, String tableName) {
        String sqlScript1 = table.getAllSqlWhere(false, true, WRAPPER_ENTITY_DOT);
        sqlScript1 = SqlScriptUtils.convertIf(sqlScript1, String.format("%s != null", WRAPPER_ENTITY), true);
        sqlScript1 += NEWLINE;
        sqlScript1 += SqlScriptUtils.convertIf(String.format("AND ${%s}", WRAPPER_SQLSEGMENT),
                String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT, WRAPPER_NONEMPTYOFWHERE), true);
        sqlScript1 = SqlScriptUtils.convertIf(sqlScript1, String.format("%s != null", WRAPPER), true);
        sqlScript1 = String.format("table_schema= schema() AND table_name= '%s'", tableName) + NEWLINE + sqlScript1;
        sqlScript1 = SqlScriptUtils.convertWhere(sqlScript1) + NEWLINE;

        String sqlScript2 = SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT),
                String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                        WRAPPER_EMPTYOFWHERE), true);
        sqlScript2 = SqlScriptUtils.convertIf(sqlScript2, String.format("%s != null", WRAPPER), true);
        return sqlScript1 + NEWLINE + sqlScript2;
    }
}
