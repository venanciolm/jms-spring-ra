package com.farmafene.jms.outbound;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionConsumer;
import jakarta.jms.ConnectionMetaData;
import jakarta.jms.Destination;
import jakarta.jms.ExceptionListener;
import jakarta.jms.JMSException;
import jakarta.jms.ServerSessionPool;
import jakarta.jms.Session;
import jakarta.jms.Topic;

public class ConnectionFromJMSSessionHandler implements Connection {

	private JMSSessionHandler jmsSh = null;

	public ConnectionFromJMSSessionHandler(JMSSessionHandler jmsSh) {
		this.jmsSh = jmsSh;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("JMSSessionHandler=").append(jmsSh);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createSession()
	 */
	@Override
	public Session createSession() throws JMSException {
		return jmsSh.getXASession();
	}

	/**
	 * 
	 * @see jakarta.jms.XAConnection#createSession(boolean, int)
	 * @see jakarta.jms.Connection#createSession(boolean, int)
	 */
	@Override
	public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
		throw new JMSException("createSession(transacted: " + transacted + ", acknowledgeMode: " + acknowledgeMode
				+ ") no soportado en entorno gestionado!");
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createSession(int)
	 */
	@Override
	public Session createSession(int sessionMode) throws JMSException {
		throw new JMSException("createSession(sessionMode: " + sessionMode + ") no soportado en entorno gestionado!");
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#getClientID()
	 */
	@Override
	public String getClientID() throws JMSException {
		return jmsSh.getJmsConnectionHandler().getConnection().getClientID();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#setClientID(java.lang.String)
	 */
	@Override
	public void setClientID(String clientID) throws JMSException {
		throw new JMSException("setClientID(clientID: " + clientID + ") no soportado en entorno gestionado!");
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#getMetaData()
	 */
	@Override
	public ConnectionMetaData getMetaData() throws JMSException {
		return jmsSh.getJmsConnectionHandler().getConnection().getMetaData();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#getExceptionListener()
	 */
	@Override
	public ExceptionListener getExceptionListener() throws JMSException {
		return jmsSh.getJmsConnectionHandler().getConnection().getExceptionListener();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#setExceptionListener(jakarta.jms.ExceptionListener)
	 */
	@Override
	public void setExceptionListener(ExceptionListener listener) throws JMSException {
		throw new JMSException("setExceptionListener(listener: " + listener + ") no soportado en entorno gestionado!");
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#start()
	 */
	@Override
	public void start() throws JMSException {
		jmsSh.getJmsConnectionHandler().getConnection().start();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#stop()
	 */
	@Override
	public void stop() throws JMSException {
		jmsSh.getJmsConnectionHandler().getConnection().stop();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#close()
	 */
	@Override
	public void close() throws JMSException {
		throw new JMSException("close() no soportado en entorno gestionado!");
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createConnectionConsumer(jakarta.jms.Destination,
	 *      java.lang.String, jakarta.jms.ServerSessionPool, int)
	 */
	@Override
	public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector,
			ServerSessionPool sessionPool, int maxMessages) throws JMSException {
		return new JMSConnectionConsumerFromJMSSessionHandler(jmsSh, jmsSh.getJmsConnectionHandler().getConnection()
				.createConnectionConsumer(destination, messageSelector, sessionPool, maxMessages));
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createSharedConnectionConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String, jakarta.jms.ServerSessionPool, int)
	 */
	@Override
	public ConnectionConsumer createSharedConnectionConsumer(Topic topic, String subscriptionName,
			String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
		return new JMSConnectionConsumerFromJMSSessionHandler(jmsSh, jmsSh.getJmsConnectionHandler().getConnection()
				.createSharedConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool, maxMessages));
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createDurableConnectionConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String, jakarta.jms.ServerSessionPool, int)
	 */
	@Override
	public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName,
			String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
		return new JMSConnectionConsumerFromJMSSessionHandler(jmsSh, jmsSh.getJmsConnectionHandler().getConnection()
				.createDurableConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool, maxMessages));
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createSharedDurableConnectionConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String, jakarta.jms.ServerSessionPool, int)
	 */
	@Override
	public ConnectionConsumer createSharedDurableConnectionConsumer(Topic topic, String subscriptionName,
			String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
		return new JMSConnectionConsumerFromJMSSessionHandler(jmsSh,
				jmsSh.getJmsConnectionHandler().getConnection().createSharedDurableConnectionConsumer(topic,
						subscriptionName, messageSelector, sessionPool, maxMessages));
	}
}
