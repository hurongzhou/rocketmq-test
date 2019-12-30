package com.example.rocketmq.service;

import com.example.rocketmq.dao.StudentMapper;
import com.example.rocketmq.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hurong
 * @date 2019/12/30 10:26 上午
 */
@Slf4j
@Service
public class StudentService {

    @Resource
    private StudentMapper studentMapper;

    public void addStudent(Student student){
        studentMapper.insertSelective(student);
    }
}
