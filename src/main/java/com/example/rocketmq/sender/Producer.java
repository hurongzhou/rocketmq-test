package com.example.rocketmq.sender;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class Producer {

    private DefaultMQProducer producer;

    @Value("${apache.rocketmq.producer.group}")
    private String producerGroup;
    @Value("${apache.rocketmq.topic}")
    private String consumerTopic;
    @Value("${apache.rocketmq.order.topic}")
    private String consumerOrderTopic;
    @Value("${apache.rocketmq.student.topic}")
    private String consumerStudentTopic;
    @Value("${apache.rocketmq.namesrvAddr}")
    private String nameServerAddr;

    @PostConstruct
    public void defaultMQProducer() {
        //生产者的组名
        producer= new DefaultMQProducer(producerGroup);
        //指定NameServer地址，多个地址以 ; 隔开
        producer.setNamesrvAddr(nameServerAddr);
        producer.setVipChannelEnabled(false);
        try {
            producer.start();
            log.info("-------->producer启动了");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public String send(String body) {
        try {
            Message message = new Message(consumerTopic, body.getBytes(RemotingHelper.DEFAULT_CHARSET));
            StopWatch stop = new StopWatch();
            stop.start();
            SendResult result = producer.send(message);
            log.info("发送响应：MsgId:" + body + "，发送状态:" + result.getSendStatus());
            stop.stop();
            return "{\"MsgId\":\""+result.getMsgId()+"\"}";
        } catch (Exception e) {
            log.error("send err", e);
        }
        return null;
    }

    @Async("taskExecutor")
    public String sendStudent(String body) {
        try {
            Message message = new Message(consumerStudentTopic, body.getBytes(RemotingHelper.DEFAULT_CHARSET));
            StopWatch stop = new StopWatch();
            stop.start();
            SendResult result = producer.send(message);
            log.info("发送响应：MsgId:" + body + "，发送状态:" + result.getSendStatus());
            stop.stop();
            return "{\"MsgId\":\""+result.getMsgId()+"\"}";
        } catch (Exception e) {
            log.error("send err", e);
        }
        return null;
    }

    public String sendOrder(String body){
        try {
            Message message = new Message(consumerOrderTopic, body.getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 发送消息并构建一个queue选择器，保证消息都进入到同一个队列中
            // 重写了MessageQueueSelector 的select方法
            SendResult sendResult = producer.send(message, (list, msg, arg) -> {
                Integer id = (Integer) arg;
                return list.get(id);
            }, 0);// 队列的下标
            log.info("发送响应：MsgId:" + sendResult.getMsgId() + "，发送状态:" + sendResult.getSendStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}