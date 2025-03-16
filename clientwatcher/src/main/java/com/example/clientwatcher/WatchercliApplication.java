package com.example.clientwatcher;

import java.util.UUID;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class WatchercliApplication implements CommandLineRunner {

	private final CuratorClientTester curatorClientTester;

	private CuratorFramework client;

	// String connectionInfo = "127.0.0.1:2181";
	// String ZK_PATH = "/someapp/somemodule/someroute/";

	@Value("${zk.conn.info}")
	public String ZK_CONN_INFO;

	@Value("${zk.path}")
	public String ZK_PATH;

	@Autowired
	private CuratorConfig curatorConfig;

	WatchercliApplication(CuratorClientTester curatorClientTester) {
		this.curatorClientTester = curatorClientTester;
	}

	public static void main(String[] args) {
		SpringApplication.run(WatchercliApplication.class, args);
	}

	@PreDestroy
	public void dest() {
		client.close();
	}

	@Override
	public void run(String... args) throws Exception {
		CuratorClientTester tester1 = new CuratorClientTester(curatorConfig);
		curatorConfig.getExecutorService().submit(tester1);

		CuratorClientTester tester2 = new CuratorClientTester(curatorConfig);
		curatorConfig.getExecutorService().submit(tester2);

		for (int ii = 0; ii < 200; ii++) {

			CuratorClientTester tester3 = new CuratorClientTester(curatorConfig);
			curatorConfig.getExecutorService().submit(tester3);
		}
	}

}
