package com.example.spring;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by zhangjiangke on 2017/5/9.
 */
@Service
public class TestController {

    @Resource
    private TestSingleton testSingleton;

    @PostConstruct
    private void init() {
        System.out.println("=========111111111111111==========11111111111111111");
    }

}
