package cn.zbq.mybatisplustest;

import cn.zbq.mybatisplustest.config.extension.PartitionInfo;
import cn.zbq.mybatisplustest.demo.entity.Student;
import cn.zbq.mybatisplustest.demo.mapper.StudentMapper;
import cn.zbq.mybatisplustest.demo.service.DynamicDataSourceService;
import cn.zbq.mybatisplustest.demo.service.StudentService;
import cn.zbq.mybatisplustest.utils.SpringBeanUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication
public class MybatisPlusTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusTestApplication.class, args);

     /*   StudentService service = SpringBeanUtils.getBean(StudentService.class);
        StudentMapper mapper = SpringBeanUtils.getBean(StudentMapper.class);
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < 33; i++) {
            Student student = new Student();
            student.setName("测试学生" + i);
            student.setGrade(2 + i);
            student.setClazz(5 + i);
            list.add(student);
        }
        service.saveBatch(list, 10, null);*/

      /*  Page<Student> page = new Page<>(1, 10);
        page.setSearchCount(false);
        Page<Student> page1 = service.page(page);
        Page<Student> page2 = service.page(page.setCurrent(2));
        log.info("总页数:{}", page2.getPages());*/


      /*  Student student = new Student();
        student.setGrade(2);
        int i = mapper.addPartitionByList("5");
        log.info("{}", i);*/

/*
        List<PartitionInfo> partitionInfoList = mapper.selectPartitionInfoList(Wrappers.<PartitionInfo>lambdaQuery().like(PartitionInfo::getName,""));
        for (PartitionInfo info : partitionInfoList) {
            log.info("{}", new Gson().toJson(info));
        }
*/




     /*   for (Student s : list) {
            log.info("遍历输出：{}", new Gson().toJson(s));
        }*/

        DynamicDataSourceService dataSourceService = SpringBeanUtils.getBean(DynamicDataSourceService.class);
        dataSourceService.test();
    }

}
