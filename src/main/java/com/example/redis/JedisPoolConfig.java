package com.example.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import redis.clients.jedis.HostAndPort;

import java.util.Set;

/**
 * Created by zhangjiangke on 2017/5/11.
 */
@PropertySource("classpath:redis.properties")
@ConfigurationProperties("redis")
@Component
public class JedisPoolConfig extends redis.clients.jedis.JedisPoolConfig {

    private Set<HostAndPort> nodes;

    private String address;

    public Set<HostAndPort> getNodes() {
        return nodes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        System.out.println("Set Address!!!!!!!!!!!!!!");
        this.address = address;
    }
}
