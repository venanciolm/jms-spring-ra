package com.farmafene.jms.outbound;

import jakarta.jms.ConnectionConsumer;
import jakarta.jms.JMSException;
import jakarta.jms.ServerSessionPool;

public class JMSConnectionConsumerFromJMSSessionHandler implements ConnectionConsumer {

	private JMSSessionHandler jmsSh = null;
	private ConnectionConsumer connectionConsumer;

	public JMSConnectionConsumerFromJMSSessionHandler(JMSSessionHandler jmsSh, ConnectionConsumer connectionConsumer) {
		this.jmsSh = jmsSh;
		this.connectionConsumer = connectionConsumer;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("jMSSessionHandler=").append(jmsSh);
		sb.append(", connectionConsumer=").append(connectionConsumer);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 
	 * @see jakarta.jms.ConnectionConsumer#getServerSessionPool()
	 */
	@Override
	public ServerSessionPool getServerSessionPool() throws JMSException {
		throw new JMSException("getServerSessionPool(), no soportado en entorno gestionado!");
	}

	/**
	 * 
	 * @see jakarta.jms.ConnectionConsumer#close()
	 */
	@Override
	public void close() throws JMSException {
		throw new JMSException("close(), no soportado en entorno gestionado!");
	}
}
