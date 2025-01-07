package com.soflyit.chattask.im.server;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.soflyit.chattask.lib.netty.server.service.ChatWebSocketServerCallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 服务启动回调<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-27 17:59
 */
@Slf4j
@Component
public class WebsocketServerCallBack implements ChatWebSocketServerCallBack, ApplicationListener<ContextClosedEvent> {


    @Value("${spring.application.name}")
    private String nettyServerName;

    private NacosDiscoveryProperties nacosDiscoveryProperties;

    private NacosServiceManager nacosServiceManager;

    private NamingService namingService;

    private int port = -1;

    @Override
    public void start(int port) {
        try {
            log.info("注册netty - websocket 服务");

            Instance instance = new Instance();
            instance.setIp(nacosDiscoveryProperties.getIp());
            instance.setPort(port);
            instance.setWeight(1.0);
            instance.setClusterName(Constants.DEFAULT_CLUSTER_NAME);
            instance.setMetadata(nacosDiscoveryProperties.getMetadata());
            namingService.registerInstance(nettyServerName + "-ws", nacosDiscoveryProperties.getGroup(), instance);
            this.port = port;
            log.info("注册netty - websocket 服务 成功");
        } catch (NacosException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (this.namingService != null && port > -1) {
            try {
                this.namingService.deregisterInstance(nettyServerName + "-ws", nacosDiscoveryProperties.getGroup(), nacosDiscoveryProperties.getIp(), port);
            } catch (NacosException e) {
                log.warn(e.getMessage(), e);
            }
        }
        log.info("注销netty - websocket 服务");
    }


    @PostConstruct
    private void init() {
        try {
            namingService = nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    @Autowired
    public void setNacosDiscoveryProperties(NacosDiscoveryProperties nacosDiscoveryProperties) {
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
    }

    @Autowired
    public void setNacosServiceManager(NacosServiceManager nacosServiceManager) {
        this.nacosServiceManager = nacosServiceManager;
    }
}
