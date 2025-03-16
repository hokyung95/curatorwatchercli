package com.example.clientwatcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Getter
public class CuratorConfig {

    @Value("${zk.conn.info}")
    public String ZK_CONN_INFO;

    @Value("${zk.path}")
    public String ZK_PATH;

    private ExecutorService executorService;

    @PostConstruct
    public void init() {
        log.info("CuratorConfig...");
        executorService = Executors.newFixedThreadPool(300);
    }
}
