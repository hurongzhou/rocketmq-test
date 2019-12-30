package com.example.rocketmq.consumer;


import com.alibaba.fastjson.JSONObject;
import com.example.rocketmq.entity.Student;
import com.example.rocketmq.service.StudentService;
import com.example.rocketmq.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class StudentConsumer implements CommandLineRunner {

    @Resource
    private StudentService studentService;

    @Value("${apache.rocketmq.student.topic}")
    private String consumerTopic;
    @Value("${apache.rocketmq.consumer.student.group}")
    private String consumerGroup;
    @Value("${apache.rocketmq.namesrvAddr}")
    private String nameServerAddr;


    /**
     * 初始化RocketMq的监听信息，渠道信息
     */
    private void messageListener(){
        DefaultMQPushConsumer consumer=new DefaultMQPushConsumer(consumerGroup);
        consumer.setInstanceName(this.getClass().getSimpleName());
        try {
            consumer.setNamesrvAddr(nameServerAddr);
            // 订阅PushTopic下Tag为push的消息
            consumer.subscribe(consumerTopic, "*");
            // 程序第一次启动从消息队列头获取数据
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            // 最小消费线程数  不写默认为20
            consumer.setConsumeThreadMin(10);
            // 最大消费线程数  不写默认为20
            consumer.setConsumeThreadMax(20);

            //在此监听中消费信息，并返回消费的状态信息
            consumer.registerMessageListener((MessageListenerConcurrently) (messageExtList, context) -> {
                LogUtil.bindLogId();
                Message message = messageExtList.get(0);
                String messageStr = new String(message.getBody());
                log.info(consumer.getInstanceName()+"接收到了消息：" + messageStr);
                Student student = JSONObject.parseObject(messageStr, Student.class);
                studentService.addStudent(student);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
            log.info("---------->consumer start");
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        this.messageListener();
    }
}

