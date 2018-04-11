package com.example.spring;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by zhangjiangke on 2017/5/9.
 */
//@Service
public class TestSingleton {


    @Resource
    private WorkerCreator workerCreator;


    @PostConstruct
    public void test() {
        TestScope testScope = workerCreator.create(10,100);
    }


}
