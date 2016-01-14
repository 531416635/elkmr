package com.zto.rocketmqtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;

public class Customer implements ApplicationListener<ContextRefreshedEvent> {

	private static Logger log = LoggerFactory.getLogger(Customer.class);
	private String nameServerAddress;
	private String consumerGroup;
	private String topicName;
	private String consumerTag;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		if (event.getApplicationContext().getParent() == null) {
			try {
				afterPropertiesSet();
			} catch (Exception e) {
				// TODO: handle exception
				log.error(e.getMessage(), e);
			}
		}
	}

	private void afterPropertiesSet() throws MQClientException {
		// TODO Auto-generated method stub
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
				getConsumerGroup());

		consumer.setNamesrvAddr(getNameServerAddress());
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.subscribe(getTopicName(), getConsumerTag());
		consumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(
					List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				// TODO Auto-generated method stub
				Map<String, List<MessageExt>> msgListMap = new HashMap<String, List<MessageExt>>();

				for (MessageExt msg : msgs) {
					if (msg.getTopic().equals(getTopicName())) {
						if (msg.getTags() == null) {
							continue;
						}
						String type = null;
						if ("1".equals(msg.getTags())) {
							// 执行tag为1的消息
							type="one";
						} else if ("2".equals(msg.getTags())) {
							// 执行tag为2的消息
							type="two";
						} else if ("3".equals(msg.getTags())) {
							// 执行tag为3的消息
							type="three";
						} else if ("4".equals(msg.getTags())) {
							// 执行tag为4的消息
							type="four";
						} else if ("5".equals(msg.getTags())) {
							// 执行tag为5的消息
							type="five";
						} else {
							continue;
						}
						if (type == null) {
							continue;
						}
						List<MessageExt> listMsg = msgListMap.get(type);
						if (listMsg == null) {
							listMsg = new ArrayList<>();
						}
						listMsg.add(msg);
						msgListMap.put(type, listMsg);
					}
				}
				consumerMessageToElasticsearch(msgListMap);
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}

		});
		consumer.start();
		log.error("MQ comsumer " + getConsumerGroup() + "  started!");
	}

	private boolean consumerMessageToElasticsearch(
			Map<String, List<MessageExt>> msgListMap) {
		// TODO Auto-generated method stub
		for (Entry<String, List<MessageExt>> entry : msgListMap.entrySet()) {
List<MessageExt> msgList=entry.getValue();
switch (entry.getKey()) {
case "one":
	
	break;

default:
	break;
}
		}

		return true;
	}

	public String getNameServerAddress() {
		return nameServerAddress;
	}

	public void setNameServerAddress(String nameServerAddress) {
		this.nameServerAddress = nameServerAddress;
	}

	public String getConsumerGroup() {
		return consumerGroup;
	}

	public void setConsumerGroup(String consumerGroup) {
		this.consumerGroup = consumerGroup;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getConsumerTag() {
		return consumerTag;
	}

	public void setConsumerTag(String consumerTag) {
		this.consumerTag = consumerTag;
	}

}
