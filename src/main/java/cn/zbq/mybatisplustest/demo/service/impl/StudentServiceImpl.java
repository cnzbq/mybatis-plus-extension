package cn.zbq.mybatisplustest.demo.service.impl;

import cn.zbq.mybatisplustest.demo.entity.Student;
import cn.zbq.mybatisplustest.demo.mapper.StudentMapper;
import cn.zbq.mybatisplustest.demo.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zbq
 * @since 2022/7/2
 */
@Slf4j
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
}
