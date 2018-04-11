package com.example.spring;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by zhangjiangke on 2017/5/9.
 */
@Service
@Scope("prototype")
public class TestScope {


    public TestScope(int i, int j) {
        System.out.println(i+"==="+j);
    }

    public TestScope(){}

    @PostConstruct
    public void init() {
        System.out.println("2222222222222222222222222222222222222222222");
    }

}
