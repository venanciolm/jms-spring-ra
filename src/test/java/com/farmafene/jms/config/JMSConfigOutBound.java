package com.farmafene.jms.config;

import org.apache.geronimo.connector.outbound.GenericConnectionManager;
import org.apache.geronimo.connector.outbound.connectionmanagerconfig.LocalTransactions;
import org.apache.geronimo.connector.outbound.connectionmanagerconfig.SinglePool;
import org.apache.geronimo.connector.outbound.connectiontracking.ConnectionTrackingCoordinator;
import org.apache.geronimo.transaction.manager.GeronimoTransactionManager;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;

import com.farmafene.jms.outbound.JMSLocalManagedConnectionFactory;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.resource.ResourceException;

public class JMSConfigOutBound {
	private static final Logger log = LoggerFactory.getLogger(JMSConfigOutBound.class);

	@Bean("ProducerJMSCF")
	public ConnectionFactory getConnectionFactoryProducer( //
			@Value("${activemq.producer.url}") String uri, //
			@Value("${activemq.producer.username}") String user, //
			@Value("${activemq.producer.password}") String password //
	) {
		JmsConnectionFactory fisicalCF = new JmsConnectionFactory(user, password, uri);
		return fisicalCF;
	}

	@Bean("ProducerTxJMSCF")
	ConnectionFactory getProducerJMSCF( //
			@Autowired @Qualifier("j2eeTrasactionManager") GeronimoTransactionManager gtm, //
			@Autowired @Qualifier("ProducerJMSCF") ConnectionFactory container, //
			@Value("${activemq.producer.maxSize}") int maxSize, //
			@Value("${activemq.producer.minSize}") int minSize,
			@Value("${activemq.producer.blockingTimeoutMilliseconds}") int blockingTimeoutMilliseconds, //
			@Value("${activemq.producer.idleTimeoutMinutes}") int idleTimeoutMinutes //
	) throws ResourceException, JMSException {
		JMSLocalManagedConnectionFactory mcf = new JMSLocalManagedConnectionFactory(container);
		GenericConnectionManager gcm = //
				new GenericConnectionManager( //
						LocalTransactions.INSTANCE, //
						// new XATransactions(true, true), //
						new SinglePool( //
								maxSize, // private int maxSize;
								minSize, // private int minSize;
								blockingTimeoutMilliseconds, // private int blockingTimeoutMilliseconds;
								idleTimeoutMinutes, // private int idleTimeoutMinutes;
								true, // private boolean matchOne;
								true, // private boolean matchAll;
								true // private boolean selectOneAssumeMatch;

						), //
						null, //
						new ConnectionTrackingCoordinator(true), //
						gtm,
						//
						mcf, //
						"JMS_OUTBOUND", //
						/* getClass().getClassLoader() */null //
				);
		ConnectionFactory wcf = (ConnectionFactory) mcf.createConnectionFactory(gcm);
		log.info("Devolviendo la ConnectionFactory: {}", wcf);
		return wcf;
	}

	@Bean("TemplateJMS")
	public JmsTemplate getTemplateJMS(//
			@Autowired @Qualifier("ProducerTxJMSCF") ConnectionFactory cf//
	) throws ResourceException {
		JmsTemplate a = new JmsTemplate(cf);
		return a;
	}

	@Bean
	public JMSProducer getProducer() throws ResourceException {
		return new JMSProducer();
	}
}
