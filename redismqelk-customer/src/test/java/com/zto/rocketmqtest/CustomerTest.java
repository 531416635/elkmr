package com.zto.rocketmqtest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.zto.helper.RedisHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath*:spring-brokertest.xml")
@Service
public class CustomerTest implements ApplicationListener<ContextRefreshedEvent> {

	private static Logger log = LoggerFactory.getLogger(Customer.class);

	@Autowired
	private customerEntity entity;

	@Resource(name = "redis134")
	private RedisHelper redisHelper;

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

	@Test
	public void afterPropertiesSet() {
		if (entity.getConsumerGroup() != null) {
			// TODO Auto-generated method stub
			DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
					entity.getConsumerGroup());

			consumer.setNamesrvAddr(entity.getNameServerAddress());
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
			String dString = entity.getConsumerTag() + "----"
					+ entity.getTopicName();
			log.info(dString);
			try {
				consumer.subscribe(entity.getTopicName(),
						entity.getConsumerTag());
			} catch (MQClientException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			consumer.registerMessageListener(new MessageListenerConcurrently() {
				public ConsumeConcurrentlyStatus consumeMessage(
						List<MessageExt> msgs,
						ConsumeConcurrentlyContext context) {
					Map<String, List<MessageExt>> msgListMap = new HashMap<String, List<MessageExt>>();
					for (MessageExt msg : msgs) {
						if (msg.getTopic().equals(entity.getTopicName())) {
							if (msg.getTags() == null) {
								continue;
							}
							String type = null;
							if ("tag".equals(msg.getTags())) {
								// 执行tag为1的消息
								type = "one";
								System.out.println(new String(msg.getBody()));
							}
							List<MessageExt> listMsg = msgListMap.get(type);
							if (listMsg == null) {
								listMsg = new ArrayList<>();
							}

							listMsg.add(msg);
							msgListMap.put(type, listMsg);
						}
					}
					// consumerMessageToElasticsearch(msgListMap);
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}

			});
			try {
				consumer.start();
				log.info("MQ comsumer < " + entity.getConsumerGroup()
						+ " > started!");
			} catch (MQClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean consumerMessageToElasticsearch(
			Map<String, List<MessageExt>> msgListMap) {
		for (Entry<String, List<MessageExt>> entry : msgListMap.entrySet()) {
			List<MessageExt> msgList = entry.getValue();
			switch (entry.getKey()) {
			case "one":
				try {
					/*
					 * String[] strings = new String[msgList.size()]; for (int i
					 * = 0; i < msgList.size(); i++) { strings[i] =
					 * msgList.get(i).toString(); } // String[] strings =
					 * msgList.toArray(new // String[msgList.size()]);
					 * redisHelper.rpush("tag", strings);
					 */
				} catch (Exception e) {
					log.info(e.toString() + e.getLocalizedMessage());
				}
				break;
			default:
				break;
			}
		}
		return true;
	}

}
