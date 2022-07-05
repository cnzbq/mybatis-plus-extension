package cn.zbq.mybatisplustest.config.extension;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * ø
 * IService扩展
 *
 * @author zbq
 * @since 2022/7/2
 */
public interface SuperService<T> extends IService<T> {

    /**
     * 分页获取数据
     *
     * @param pageSize     分页大小
     * @param queryWrapper 查询条件
     * @return 获取数据function
     */
    default Function<Integer, List<T>> getList(int pageSize, Wrapper<T> queryWrapper) {
        return current -> page(new Page<>(current, pageSize, false), queryWrapper).getRecords();
    }

    /**
     * 批量保存数据
     * <p>为了扩展性，这里允许传入子类，但子类会转型父类，可能存在字段丢失</p>
     *
     * @param entityList      数据list
     * @param batchSize       每批大小
     * @param successCallBack 成功回调函数
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    default void saveBatch(List<? extends T> entityList, int batchSize, Consumer<List<? extends T>> successCallBack) {
        BaseMapper<T> mapper = getBaseMapper();
        if (mapper instanceof SuperMapper) {
            SuperMapper<T> superMapper = (SuperMapper<T>) mapper;
            process(entityList, batchSize, l -> superMapper.insertBatchSomeColumn((List<T>) l));
        } else {
            saveBatch((List<T>) entityList, batchSize);
        }

        if (Objects.nonNull(successCallBack)) {
            successCallBack.accept(entityList);
        }
    }

    /**
     * 添加表分区
     *
     * @param partValue 分区值
     */
    default void addPartitionByList(String partValue) {
        BaseMapper<T> mapper = getBaseMapper();
        if (mapper instanceof SuperMapper) {
            ((SuperMapper<T>) mapper).addPartitionByList(partValue);
        } else {
            throw new UnsupportedOperationException("当前实现不支持该方法，mapper类需要继承SuperMapper!");
        }
    }

    /**
     * 获取表分区信息
     *
     * @return 分区信息list
     */
    default List<PartitionInfo> getPartitionInfoList() {
        return getPartitionInfoList(null);
    }

    /**
     * 获取表分区信息
     *
     * @return 分区信息list
     */
    default List<PartitionInfo> getPartitionInfoList(Wrapper<PartitionInfo> queryWrapper) {
        BaseMapper<T> mapper = getBaseMapper();
        if (mapper instanceof SuperMapper) {
            return ((SuperMapper<T>) mapper).selectPartitionInfoList(queryWrapper);
        } else {
            throw new UnsupportedOperationException("当前实现不支持该方法，mapper类需要继承SuperMapper!");
        }
    }

    default <V> void process(List<V> source, int size, Consumer<List<V>> consumer) {
        int max = source.size();
        for (int i = 0; i < max; i += size) {
            consumer.accept(source.subList(i, Math.min(i + size, max)));
        }
    }
}
