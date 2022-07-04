package cn.zbq.mybatisplustest.config.extension;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
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

    /**
     * 查询表分区信息
     *
     * @return 分区信息list
     */
    List<PartitionInfo> selectPartitionInfoList(@Param(Constants.WRAPPER) Wrapper<PartitionInfo> queryWrapper);
}
