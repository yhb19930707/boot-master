package com.qdone;

import org.springframework.boot.CommandLineRunner;
//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*import org.springframework.scheduling.annotation.EnableScheduling;*/
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动器
 */
@SpringBootApplication
@EnableTransactionManagement
/*@EnableScheduling*/
/*@MapperScan("com.qdone.module.mapper")*/
public class StartUpApplication implements CommandLineRunner{

	public static void main(String[] args) throws Exception {
		
		SpringApplication.run(StartUpApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.err.println("呀呼！系统初始化完成了！");
	}

}
