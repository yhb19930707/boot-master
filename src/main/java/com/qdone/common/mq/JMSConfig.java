package com.qdone.common.mq;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

/**
 * @author 付为地 
 * 配置jms 同时支持queue和topic消息
 * 参考资料:https://spring.io/guides/gs/messaging-jms/
 */
@Configuration
public class JMSConfig {
	
	/**
	 * 队列类型的queue对应JmsListenerContainer
	 * @param activeMQConnectionFactory
	 *        ActiveMQ连接配置
	 * @return 
	 *       队列JmsListenerContainer
	 */
	@Bean(name = "queueListenerContainerFactory")
	public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ConnectionFactory activeMQConnectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setPubSubDomain(false);//true开启topic,false开启queue
		factory.setConnectionFactory(activeMQConnectionFactory);
		factory.setMessageConverter(jacksonJmsMessageConverter());
		return factory;
	}
    
	/**
	 * 消息体序列化
	 * @return 
	 *       信息解析器
	 */
	@Bean 
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}
	
	/**
	 * 发布订阅类型的topic对应JmsListenerContainer
	 * @param activeMQConnectionFactory
	 *        ActiveMQ连接配置
	 * @return 
	 *       订阅JmsListenerContainer
	 */
    @Bean(name="topicListenerContainerFactory")
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setPubSubDomain(true);//true开启topic,false开启queue
        bean.setConnectionFactory(activeMQConnectionFactory);
        return bean;
    }
    
    /**
     * @param activeMQConnectionFactory 连接工厂
     * @return 通用队列消息监听器
     */
    @Bean
	public DefaultMessageListenerContainer listenerContainer(ConnectionFactory activeMQConnectionFactory){
    	DefaultMessageListenerContainer m =new DefaultMessageListenerContainer();
		m.setConnectionFactory(activeMQConnectionFactory);
		Destination d = new ActiveMQQueue("*");//*表示通配所有队列名称,目前针对单个名字有效,比如:qghappy,针对qghappy.name无效
		m.setDestination(d);
		m.setMessageListener(new QueueMessageListener());
		return m;		
	}

}
