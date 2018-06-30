package com.qdone;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

/**
 * 启动器 
 *  Session集群方案
 *   1.@EnableRedisHttpSession+spring-session-data-redis:配置session采用redis同步
 *   2.@EnableRedissonHttpSession+spring-session:配置redisson同步session
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableRedisHttpSession
public class StartUpApplication implements CommandLineRunner{
	
	final Logger log=LoggerFactory.getLogger(StartUpApplication.class);
	
    @Autowired
	ConfigurableEnvironment env;
    @Autowired
    InitConfiguration init;
	public static void main(String[] args) throws Exception {
		SpringApplication.run(StartUpApplication.class, args);
		/* //编码激活不同profile配置
		 SpringApplication app = new SpringApplication(StartUpApplication.class);
	     app.setAdditionalProfiles("uat");   // dev 或uat
	     app.run(args);*/
	}

	@Override
	public void run(String... args) throws Exception {
		String[] profiles=env.getActiveProfiles();
		log.info("===========系统启动完成！"+"运行环境:["+StringUtils.arrayToCommaDelimitedString(profiles)+"] IP:["+init.getServerIp()+"] PORT:["+init.getServerPort()+"]===========");
	}
	/*
	 * 获取服务器启动参数
	 */
	@Component
	public class InitConfiguration implements ApplicationListener<WebServerInitializedEvent> {
	    private int serverPort;
	    @Override
	    public void onApplicationEvent(WebServerInitializedEvent event) {
	        this.serverPort = event.getWebServer().getPort();
	    }
	    public int getServerPort() {
	        return this.serverPort;
	    }
		public String getServerIp() throws UnknownHostException {
			return InetAddress.getLocalHost().getHostAddress();
		}
	}

}
