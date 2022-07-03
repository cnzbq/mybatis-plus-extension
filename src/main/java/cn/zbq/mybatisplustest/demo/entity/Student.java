package cn.zbq.mybatisplustest.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author zbq
 * @since 2022/7/2
 */
@Data
@TableName("students")
public class Student {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer grade;
    @TableField("class")
    private Integer clazz;
}
