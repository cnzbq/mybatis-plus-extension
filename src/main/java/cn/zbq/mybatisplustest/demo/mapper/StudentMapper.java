package cn.zbq.mybatisplustest.demo.mapper;

import cn.zbq.mybatisplustest.config.extension.SuperMapper;
import cn.zbq.mybatisplustest.demo.entity.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zbq
 * @since 2022/7/2
 */
@Mapper
public interface StudentMapper extends SuperMapper<Student> {
}
