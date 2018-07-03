package com.qdone.common.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author 付为地
 *    通用队列消息监听器
 */
public class QueueMessageListener implements MessageListener {
	
    Logger logger = LoggerFactory.getLogger(QueueMessageListener.class);
    
	/**
	 * @param message
	 *        队列消息
	 */
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage tm = (TextMessage) message;
			ActiveMQDestination queues=(ActiveMQDestination)message.getJMSDestination();
			logger.info("接收到队列:{},消息:{}",queues.getPhysicalName(),tm.getText());
		} catch (JMSException e) {
			e.printStackTrace();
			logger.error("接收队列消息异常:{}",e);
		}
	}
 
}
