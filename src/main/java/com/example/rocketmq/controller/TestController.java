package com.example.rocketmq.controller;

import com.example.rocketmq.sender.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/push")
    public void testMq(String s){
        try {
            for (int i = 0;i < 20; i++){
                producer.sendOrder(s+i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void health(){
        log.info("health success!");
    }

}
