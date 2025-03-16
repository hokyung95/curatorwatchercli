package com.example.clientwatcher;

import java.util.UUID;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class CuratorClientTester implements Runnable {

    // private final WatchercliApplication watchercliApplication;

    private CuratorFramework client;
    private CuratorConfig curatorConfig;
    private String clientId;

    public CuratorClientTester(CuratorConfig curatorConfig) {
        this.curatorConfig = curatorConfig;
    }

    // Curator retry policy setting and client start...
    // and create ephemeral node on path....
    @Override
    public void run() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(300, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(curatorConfig.ZK_CONN_INFO)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace("base")
                .build();
        client.start();

        clientId = UUID.randomUUID().toString();
        try {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(curatorConfig.ZK_PATH + clientId,
                    "sometdata".getBytes());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (true) {
            try {
                // log.info("ClientId: " + this.clientId);
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
