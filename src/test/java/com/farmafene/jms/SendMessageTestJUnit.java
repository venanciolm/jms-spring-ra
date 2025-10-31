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
import com.farmafene.jms.config.JMSConfigOutBound;
import com.farmafene.jms.config.JMSProducer;

import jakarta.jms.ConnectionFactory;
import jakarta.resource.spi.ResourceAdapter;

@SpringJUnitConfig( //
		classes = { //
				CargaProps.class, //
				GeronimoTXBeans.class, //
				JMSConfigOutBound.class //
		} //
)
public class SendMessageTestJUnit {
	private static final Logger LOGGER = LoggerFactory.getLogger(SendMessageTestJUnit.class);

	@Autowired(required = false)
	private ResourceAdapter ra;
	@Autowired
	@Qualifier("ProducerJMSCF")
	private ConnectionFactory cf;
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
		LOGGER.info("ConnectionFactory: {}", cf);
		LOGGER.info("ConnectionFactory: {}", txCf);
		LOGGER.info("         Template: {}", template);
		LOGGER.info("            Queue: {}", queue);
		try {
			Scanner terminalInput = new Scanner(System.in);
			LOGGER.info("Wait for a line!");
			prod.send();
			LOGGER.info("Out: {}", terminalInput.nextLine());
			LOGGER.info("Wait for a line!");
			prod.send();
			LOGGER.info("Out: {}", terminalInput.nextLine());
			terminalInput.close();
		} catch (Exception e) {
			LOGGER.error("Error en el proceso", e);
			Assert.isNull(e, "El valor debe ser null");
		} finally {
			LOGGER.info("Fin del proceso...");
		}
	}
}
