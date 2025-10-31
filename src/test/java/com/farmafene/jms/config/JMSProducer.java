package com.farmafene.jms.config;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;

import jakarta.transaction.Transactional;

public class JMSProducer {
	@Autowired
	@Qualifier("TemplateJMS")
	private JmsTemplate template;
	@Value("${activemq.producer.queue}")
	private String queue;

	@Transactional(rollbackOn = Throwable.class)
	public void send() {
		template.convertAndSend(queue, "Msg: " + new Timestamp(System.currentTimeMillis()));
	}
}
