package com.farmafene.jms;

import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.Assert;

import com.farmafene.jms.config.CargaProps;
import com.farmafene.jms.config.GeronimoTXBeans;
import com.farmafene.jms.config.JMSConfigInboundArtemis;
import com.farmafene.jms.config.JMSConfigOutBoundArtemis;
import com.farmafene.jms.config.JMSProducer;
import com.farmafene.jms.config.VMBrokerProg;

import jakarta.jms.ConnectionFactory;
import jakarta.resource.spi.ResourceAdapter;

@SpringJUnitConfig( //
		classes = { //
				CargaProps.class, //
				GeronimoTXBeans.class, //
				VMBrokerProg.class, //
				JMSConfigInboundArtemis.class, //
				JMSConfigOutBoundArtemis.class //

		} //
)
public class RunListenerArtemisTestJUnit {
	private static final Logger LOGGER = LoggerFactory.getLogger(RunListenerArtemisTestJUnit.class);

	@Autowired(required = false)
	private ResourceAdapter ra;
	@Autowired(required = false)
	@Qualifier("ConsumerJMSCF")
	private ConnectionFactory ccf;

	@Autowired
	@Qualifier("ProducerJMSCF")
	private ConnectionFactory pcf;
	@Autowired
	@Qualifier("ProducerTxJMSCF")
	private ConnectionFactory txCf;
	@Autowired
	@Qualifier("TemplateJMS")
	private JmsTemplate template;

	@Value("${activemq.producer.queue}")
	private String queue;
	@Autowired
	private JMSProducer prod;

	@Test
	public void test01() {
		LOGGER.info("Probando lo básico ..");
		LOGGER.info("  ResourceAdapter: {}", ra);
		LOGGER.info("ConnectionFactory: {}", ccf);
		LOGGER.info("ConnectionFactory: {}", pcf);
		try {
			Scanner terminalInput = new Scanner(System.in);
			for (int i = 0; i < 3; i++) {
				prod.send();
				LOGGER.info("Out: {}", terminalInput.nextLine());
			}
			terminalInput.close();
		} catch (Exception e) {
			LOGGER.error("Error en el proceso", e);
			Assert.isNull(e, "El valor debe ser null");
		} finally {
			LOGGER.info("Fin del proceso...");
		}
	}
}
