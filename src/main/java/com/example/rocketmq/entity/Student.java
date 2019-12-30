package com.example.rocketmq.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tb_student
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student implements Serializable {
    private Integer id;

    private String stuName;

    private String stuPhone;

    private Integer sex;

    private String avatarUrl;

    private String clazz;

    private Integer grade;

    private String homeAddress;

    private static final long serialVersionUID = 1L;
}