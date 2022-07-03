package cn.zbq.mybatisplustest.config.extension;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * BaseMapper扩展
 *
 * @author zbq
 * @since 2022/7/2
 */
public interface SuperMapper<T> extends BaseMapper<T> {

    /**
     * 批量新增数据,自选字段 insert
     */
    int insertBatchSomeColumn(@Param("list") List<T> entityList);

    /**
     * 添加list分区
     *
     * @param value 分区值
     * @return /
     */
    int addPartitionByList(@Param("v") String value);
}
