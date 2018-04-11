package com.example.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by zhangjiangke on 2017/5/5.
 */
@PropertySource("classpath:config.properties")
//@ConfigurationProperties
@Component
public class GlobalConfigInfo {

    // 服务器ip
    @Value("${server.ip}")
    private String serverIp;

    // 服务器端口
    @Value("${server.port}")
    private int serverPort = 8002;

    // 心跳检测；单位秒
    @Value("${server.heartbeat.time}")
    private int heartbeatTime = 30;

    // handler class path
    @Value("${server.handler.classPath}")
    private String handlerClasspath;

    @Value("${logic.server.redis.cluster.address}")
    private String redisClusterAddress;

    // 服务器资源每个多久上报一次；Quartz配置;分钟
    @Value("${server.resource.expire}")
    private String serverResourceExpire;

    // 消息队列host
    @Value("${mq.host}")
    private String mqHost;

    // 消息队列port
    @Value("${mq.port}")
    private int mqPort;

    // 消息队列virtualHost
    @Value("${mq.virtualHost}")
    private String mqVirtualHost;

    // 消息队列账号
    @Value("${mq.username}")
    private String mqUsername;

    // 消息队列密码
    @Value("${mq.password}")
    private String mqPassword;

    // 消息队列名称
    @Value("${mq.queueName}")
    private String mqQueueName;

    // apns消息推送队列名称
    @Value("${mq.apns.queuName}")
    private String apnsQueueName;

    // 聊天消息分库模值
    @Value("${msg.db.modulo}")
    private int msgDbModulo;

    // 聊天消息分表模值
    @Value("${msg.table.modulo}")
    private int msgTableModulo;

    // mongodb数据库名称
    @Value("${db_name}")
    private String dbName;
    
    // mongodb 离线消息数据库名称
    @Value("${db_msg_name}")
    private String dbMsgName;

    // 服务资源redis上报key
    @Value("${server.resource.key}")
    private String serverResource;

    // zookeeper连接地址;多个用逗号(,)分开
    @Value("${zookeeper.address}")
    private String zkAddress;

    // gk zookeeper根
    @Value("${zookeeper.path}")
    private String zkPath;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getHeartbeatTime() {
        return heartbeatTime;
    }

    public void setHeartbeatTime(int heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }

    public String getHandlerClasspath() {
        return handlerClasspath;
    }

    public void setHandlerClasspath(String handlerClasspath) {
        this.handlerClasspath = handlerClasspath;
    }

    public String getRedisClusterAddress() {
        return redisClusterAddress;
    }

    public void setRedisClusterAddress(String redisClusterAddress) {
        this.redisClusterAddress = redisClusterAddress;
    }

    public String getServerResourceExpire() {
        return serverResourceExpire;
    }

    public void setServerResourceExpire(String serverResourceExpire) {
        this.serverResourceExpire = serverResourceExpire;
    }

    public String getMqHost() {
        return mqHost;
    }

    public void setMqHost(String mqHost) {
        this.mqHost = mqHost;
    }

    public int getMqPort() {
        return mqPort;
    }

    public void setMqPort(int mqPort) {
        this.mqPort = mqPort;
    }

    public String getMqVirtualHost() {
        return mqVirtualHost;
    }

    public void setMqVirtualHost(String mqVirtualHost) {
        this.mqVirtualHost = mqVirtualHost;
    }

    public String getMqUsername() {
        return mqUsername;
    }

    public void setMqUsername(String mqUsername) {
        this.mqUsername = mqUsername;
    }

    public String getMqPassword() {
        return mqPassword;
    }

    public void setMqPassword(String mqPassword) {
        this.mqPassword = mqPassword;
    }

    public String getMqQueueName() {
        return mqQueueName;
    }

    public void setMqQueueName(String mqQueueName) {
        this.mqQueueName = mqQueueName;
    }

    public String getApnsQueueName() {
        return apnsQueueName;
    }

    public void setApnsQueueName(String apnsQueueName) {
        this.apnsQueueName = apnsQueueName;
    }

    public int getMsgDbModulo() {
        return msgDbModulo;
    }

    public void setMsgDbModulo(int msgDbModulo) {
        this.msgDbModulo = msgDbModulo;
    }

    public int getMsgTableModulo() {
        return msgTableModulo;
    }

    public void setMsgTableModulo(int msgTableModulo) {
        this.msgTableModulo = msgTableModulo;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbMsgName() {
        return dbMsgName;
    }

    public void setDbMsgName(String dbMsgName) {
        this.dbMsgName = dbMsgName;
    }

    public String getServerResource() {
        return serverResource;
    }

    public void setServerResource(String serverResource) {
        this.serverResource = serverResource;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getZkPath() {
        return zkPath;
    }

    public void setZkPath(String zkPath) {
        this.zkPath = zkPath;
    }
}
