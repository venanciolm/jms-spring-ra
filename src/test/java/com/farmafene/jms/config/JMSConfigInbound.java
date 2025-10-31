package com.farmafene.jms.config;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.geronimo.connector.GeronimoBootstrapContext;
import org.apache.geronimo.connector.work.GeronimoWorkManager;
import org.apache.geronimo.connector.work.WorkContextHandler;
import org.apache.geronimo.transaction.manager.GeronimoTransactionManager;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.listener.endpoint.JmsMessageEndpointManager;
import org.springframework.test.context.event.BeforeTestExecutionEvent;

import com.farmafene.jms.SpringMessageListener;
import com.farmafene.jms.outbound.XAConnectionFactoryFromConnectionFactory;
import com.farmafene.jms.ra.JMSActivationSpec;
import com.farmafene.jms.ra.JMSMessageEndpointFactory;
import com.farmafene.jms.ra.JMSResourceAdapter;

import jakarta.jms.MessageListener;
import jakarta.jms.XAConnectionFactory;
import jakarta.resource.spi.ResourceAdapterInternalException;

public class JMSConfigInbound {
	private static final Logger log = LoggerFactory.getLogger(JMSConfigInbound.class);
	@Bean("ConsumerJMSCF")
	public XAConnectionFactory getContainer( //
			@Value("${activemq.consumer.url}") String uri, //
			@Value("${activemq.consumer.username}") String user, //
			@Value("${activemq.consumer.password}") String password //
	) {
		JmsConnectionFactory fisicalCF = new JmsConnectionFactory(user, password, uri);
		XAConnectionFactory xacf = new XAConnectionFactoryFromConnectionFactory(fisicalCF);
		return xacf;
	}

	@Bean("SpringResourceAdapter")
	public JMSResourceAdapter getJMSResourceAdapter( //
	) throws Exception {
		JMSResourceAdapter ra = new JMSResourceAdapter();
		return ra;
	}

	@Bean
	public DisposableBean getDisposableJMSResourceAdapter( //
			@Autowired @Qualifier("SpringResourceAdapter") JMSResourceAdapter ra, //
			@Autowired @Qualifier("ConsumerWM") GeronimoWorkManager gwm //
	) {
		return new DisposableBean() {

			@Override
			public void destroy() throws Exception {
				ra.stop();
				gwm.doStop();
			}
		};
	}

	@Bean("SpringListener")
	public SpringMessageListener getSpringMessageListener( //
	) {
		return new SpringMessageListener();
	}

	@Bean("ConsumerMEPF")
	public JmsMessageEndpointManager jmsMessageEndpointManager( //
			@Autowired @Qualifier("j2eeTrasactionManager") GeronimoTransactionManager gtm, //
			@Autowired @Qualifier("SpringListener") MessageListener myMessageListener, //
			@Autowired @Qualifier("SpringResourceAdapter") JMSResourceAdapter ra, //
			@Autowired @Qualifier("ConsumerJMSCF") XAConnectionFactory container, //
			@Value("${activemq.consumer.queue}") String queue, //
			@Value("${activemq.consumer.maxSessions}") int maxSessions //

	) throws Exception {
		JMSActivationSpec spec = new JMSActivationSpec();
		spec.setLocalTransactions(true);
		spec.setNumSessions(maxSessions);
		spec.setQueue(queue);
		spec.setXAConnectionFactory(container);
		/**
		 * spec.setXAConnectionFactory(new XAConnectionFactoryFromConnectionFactory( new
		 * JmsConnectionFactory(user, password, url)));
		 */
		JmsMessageEndpointManager endpointManager = new JmsMessageEndpointManager();
		endpointManager.setAutoStartup(false);
		endpointManager.setResourceAdapter(ra);
		endpointManager.setActivationSpec(spec);
		endpointManager.setMessageEndpointFactory(//
				new JMSMessageEndpointFactory( //
						gtm, //
						myMessageListener //
				) //
		);
		return endpointManager;
	}

	@Bean("ConsumerWM")
	public GeronimoWorkManager getGeronimoWorkManager( //
			@Value("${activemq.consumer.maxSessions}") int size //
	) {
		@SuppressWarnings("rawtypes")
		Collection<WorkContextHandler> wchs = Collections.<WorkContextHandler>emptyList();
		ExecutorService scheduledWorkExecutorPool = Executors.newFixedThreadPool(size);
		GeronimoWorkManager gwm = new GeronimoWorkManager(//
				null, // syncWorkExecutorPool
				null, // startWorkExecutorPool
				scheduledWorkExecutorPool, // scheduledWorkExecutorPool
				wchs //
		);
		return gwm;
	}

	@Bean
	public ApplicationListener<BeforeTestExecutionEvent> onStartJMS(//
			@Autowired @Qualifier("SpringResourceAdapter") JMSResourceAdapter ra, //
			@Autowired @Qualifier("ConsumerMEPF") JmsMessageEndpointManager mepf, //
			@Autowired @Qualifier("ConsumerWM") GeronimoWorkManager gwm, //
			@Autowired @Qualifier("j2eeTrasactionManager") GeronimoTransactionManager gtm //
	) {
		StringBuilder sb = new StringBuilder();
		String lf = System.lineSeparator();
		sb.append(lf).append("/+-----------------------------------+");
		sb.append(lf).append(" | JMSConsumer ApplicationReadyEvent |");
		sb.append(lf).append(" +-----------------------------------+");
		log.info("{}", sb);
		GeronimoBootstrapContext gbc = new GeronimoBootstrapContext( //
				gwm, //
				gtm, //
				gtm //
		);
		return new ApplicationListener<BeforeTestExecutionEvent>() {
			/**
			 * 
			 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
			 */
			@Override
			public void onApplicationEvent(BeforeTestExecutionEvent event) {
				try {
					log.info("Conectando el WM");
					gwm.doStart();
					log.info("Arrancando el RA");
					ra.start(gbc);
					log.info("Arrancando el mepf");
					mepf.start();
				} catch (ResourceAdapterInternalException e) {
					log.error("Error en el arranque!!", e);
				} catch (Exception e) {
					log.error("Error en el arranque!!", e);
				}
			}
		};
	}

}
