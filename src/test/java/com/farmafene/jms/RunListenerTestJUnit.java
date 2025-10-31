package com.farmafene.jms;

import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.Assert;

import com.farmafene.jms.config.CargaProps;
import com.farmafene.jms.config.GeronimoTXBeans;
import com.farmafene.jms.config.JMSConfigInbound;

import jakarta.jms.ConnectionFactory;
import jakarta.resource.spi.ResourceAdapter;

@SpringJUnitConfig( //
		classes = { //
				CargaProps.class, //
				GeronimoTXBeans.class, //
				JMSConfigInbound.class //
		} //
)
public class RunListenerTestJUnit {
	private static final Logger LOGGER = LoggerFactory.getLogger(RunListenerTestJUnit.class);

	@Autowired(required = false)
	private ResourceAdapter ra;
	@Autowired(required = false)
	private ConnectionFactory cf;

	@Test
	public void test01() {
		LOGGER.info("Probando lo básico ..");
		LOGGER.info("  ResourceAdapter: {}", ra);
		LOGGER.info("ConnectionFactory: {}", cf);
		try {
			Scanner terminalInput = new Scanner(System.in);
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
