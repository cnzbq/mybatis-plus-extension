package cn.zbq.mybatisplustest.config.extension;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 分区信息
 *
 * @author zbq
 * @since 2022/7/4
 */
@Data
public class PartitionInfo {

    /**
     * 名称
     */
    @TableField("partition_name")
    private String name;

    /**
     * 分区列
     */
    @TableField("partition_expression")
    private String expression;

    /**
     * 描述
     */
    @TableField("partition_description")
    private String description;

    /**
     * 表行数
     */
    @TableField("table_rows")
    private String tableRows;
}
