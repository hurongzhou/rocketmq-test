package com.example.rocketmq.controller;

import com.alibaba.fastjson.JSON;
import com.example.rocketmq.entity.Student;
import com.example.rocketmq.sender.Producer;
import com.example.rocketmq.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hurong
 * @date 2019/12/27 3:20 下午
 */
@Slf4j
@RestController
public class TestController {

    @Resource
    private Producer producer;
    @Resource
    private StudentService studentService;

    @RequestMapping("/push")
    public void testMq(String s){
        try {
            for (int i = 0;i < 20; i++){
                producer.send(s+i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/health", method = RequestMethod.POST)
    public void health(){
        log.info("health success!");
    }

    @RequestMapping("/addStudent")
    public Student addStudent(){
        for (int i = 1; i <= 100000; i++){
            Student student = new Student();
            int num = (int) (Math.random()*10000);
            student.setAvatarUrl("http://avatar.com"+num);
            student.setClazz(i+"班级");
            student.setGrade(i);
            student.setHomeAddress("北京市海淀区中关村"+i+"号");
            student.setSex(num > 500 ? 1 : 0);
            student.setStuName("学生名字"+num);
            student.setStuPhone("133325"+num);
            producer.sendStudent(JSON.toJSONString(student));
        }
        log.info("send to queue finish!");
        return null;
    }

}
