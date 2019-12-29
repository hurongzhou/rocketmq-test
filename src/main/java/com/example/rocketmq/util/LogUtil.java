package com.example.rocketmq.util;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author hurong
 * @date 2019/12/27 7:47 下午
 */
public class LogUtil {

    public static void bindLogId(){
        MDC.put("LOG_ID", UUID.randomUUID().toString().replace("-", ""));
    }
}
