package com.example.netty;

import com.example.common.GlobalConfigInfo;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by zhangjiangke on 2017/5/5.
 */
@Service
public class Test {

    @Resource
    private GlobalConfigInfo globalConfigInfo;

    @PostConstruct
    private void init() {
        System.out.println(globalConfigInfo.getApnsQueueName());
        System.out.println(globalConfigInfo.getZkAddress());
    }


}
