package com.example.extend;

import com.alibaba.fastjson.JSON;
import com.example.redis.JedisPoolConfig;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by zhangjiangke on 2017/5/9.
 */
//@Repository
public class BaseDao {

    @Resource
    private JedisPoolConfig jedisPoolConfig;

    @PostConstruct
    private void init() {
        System.out.println(JSON.toJSONString(jedisPoolConfig));
        System.out.println("============11111111111111111111111");

    }

    public BaseDao(){
        System.out.println("BASEDAO ------------------------------");
    }


}
