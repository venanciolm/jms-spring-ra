package com.farmafene.jms.outbound;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.JMSException;
import jakarta.jms.XAConnectionFactory;

public class JMSSessionHandlerFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSSessionHandlerFactory.class);
	private List<JMSConnectionHandler> handlers = new CopyOnWriteArrayList<JMSConnectionHandler>();
	private JMSManagedConnectionFactory managedConnectionFactory;
	private XAConnectionFactory xAConnectionFactory;
	Set<JMSSessionHandler> sHandlers = new CopyOnWriteArraySet<JMSSessionHandler>();
	private int index = 0;

	public JMSSessionHandlerFactory(JMSManagedConnectionFactory jmsManagedConnectionFactory,
			XAConnectionFactory xamcf) {
		this.managedConnectionFactory = jmsManagedConnectionFactory;
		this.xAConnectionFactory = xamcf;
	}

	public JMSSessionHandler getHandler() throws JMSException {
		synchronized (this) {
			index = index % managedConnectionFactory.getNumConnections();
			if (!validateConnections(index)) {
				JMSConnectionHandler cHandler = generaJMSConnectionHandler();
				if (null != cHandler) {
					handlers.add(cHandler);
				}
			}
			JMSConnectionHandler con = //
					(handlers.size() > index) //
							? handlers.get(index++) // Pool lleno
							: handlers.isEmpty() // ¿Errores?
									? //
									null // pues .. no devolvemos nada
									: handlers.get(handlers.size() - 1); // pillamos la ultima generada...
			if (null == con) {
				throw new JMSException("No se ha podido crear la conexión", "CONNECTIONS_ERROR");
			}
			JMSSessionHandler sh = new JMSSessionHandler(con, con.getConnection().createXASession());
			sHandlers.add(sh);
			return sh;
		}
	}

	private boolean validateConnections(int index) {
		boolean validateConnections = false;
		if (handlers.size() > index) {
			validateConnections = true;
		}
		return validateConnections;
	}

	private JMSConnectionHandler generaJMSConnectionHandler() {
		JMSConnectionHandler cHandler = null;
		try {
			cHandler = new JMSConnectionHandler(xAConnectionFactory.createXAConnection());
			cHandler.start();
		} catch (JMSException e) {
			LOGGER.error("Error en la generación del pool", e);
		}
		return cHandler;
	}

}
