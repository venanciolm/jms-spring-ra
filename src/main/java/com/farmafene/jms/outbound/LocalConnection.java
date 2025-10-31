package com.farmafene.jms.outbound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionConsumer;
import jakarta.jms.ConnectionMetaData;
import jakarta.jms.Destination;
import jakarta.jms.ExceptionListener;
import jakarta.jms.JMSException;
import jakarta.jms.ServerSessionPool;
import jakarta.jms.Session;
import jakarta.jms.Topic;

public class LocalConnection implements Connection {
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalConnection.class);
	private ManagedJMSConnection managedJMSConnection = null;
	private Connection jmsConnection;

	public LocalConnection(ManagedJMSConnection managedJMSConnection, Connection jmsConnection) {
		this.managedJMSConnection = managedJMSConnection;
		this.jmsConnection = jmsConnection;
		try {
			this.jmsConnection.setExceptionListener( //
					new ExceptionListener() {
						/**
						 * 
						 * @see jakarta.jms.ExceptionListener#onException(jakarta.jms.JMSException)
						 */
						@Override
						public void onException(JMSException exception) {
							LocalConnection.this.managedJMSConnection.connectionErrorOccurred(LocalConnection.this);
						}
					});
		} catch (JMSException e) {
			LOGGER.warn("Problemas en el establecimiento del ExceptionListener", e);
		}
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#start()
	 */
	@Override
	public void start() throws JMSException {
		LOGGER.trace("start()");
		jmsConnection.start();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#stop()
	 */
	@Override
	public void stop() throws JMSException {
		LOGGER.trace("stop()");
		jmsConnection.stop();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#close()
	 */
	@Override
	public void close() throws JMSException {
		LOGGER.trace("close()");
		managedJMSConnection.connectionClosed(this);
	}

	// --------------------------------------------------------
	/**
	 * 
	 * @see jakarta.jms.Connection#createSession(boolean, int)
	 */
	@Override
	public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
		return this.createSession();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createSession(int)
	 */
	@Override
	public Session createSession(int sessionMode) throws JMSException {
		return this.createSession();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createSession()
	 */
	@Override
	public Session createSession() throws JMSException {
		Session sess = jmsConnection.createSession(true, Session.SESSION_TRANSACTED);
		managedJMSConnection.setSession(sess);
		return new LocalSession(managedJMSConnection);
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#getClientID()
	 */
	@Override
	public String getClientID() throws JMSException {
		LOGGER.trace("getClientID()");
		return jmsConnection.getClientID();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#setClientID(java.lang.String)
	 */
	@Override
	public void setClientID(String clientID) throws JMSException {
		LOGGER.trace("setClientID(String: {})", clientID);
		jmsConnection.setClientID(clientID);
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#getMetaData()
	 */
	@Override
	public ConnectionMetaData getMetaData() throws JMSException {
		LOGGER.trace("getMetaData()");
		return jmsConnection.getMetaData();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#getExceptionListener()
	 */
	@Override
	public ExceptionListener getExceptionListener() throws JMSException {
		LOGGER.trace("getExceptionListener()");
		return jmsConnection.getExceptionListener();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#setExceptionListener(jakarta.jms.ExceptionListener)
	 */
	@Override
	public void setExceptionListener(ExceptionListener listener) throws JMSException {
		LOGGER.trace("setExceptionListener(ExceptionListener: {})", listener);
		jmsConnection.setExceptionListener(listener);
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createConnectionConsumer(jakarta.jms.Destination,
	 *      java.lang.String, jakarta.jms.ServerSessionPool, int)
	 */
	@Override
	public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector,
			ServerSessionPool sessionPool, int maxMessages) throws JMSException {
		LOGGER.trace(
				"setExceptionListener(createConnectionConsumer(Destination: {}, String: {}, ServerSessionPool: {}, int{})",
				destination, messageSelector, sessionPool, maxMessages);
		return jmsConnection.createConnectionConsumer(destination, messageSelector, sessionPool, maxMessages);
	}

	@Override
	public ConnectionConsumer createSharedConnectionConsumer(Topic topic, String subscriptionName,
			String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
		LOGGER.trace(
				"createSharedConnectionConsumer(Topic: {}, String: {}, String: {}, ServerSessionPool: {}, int: {})",
				topic, subscriptionName, messageSelector, sessionPool, maxMessages);
		return jmsConnection.createSharedConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool,
				maxMessages);
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createDurableConnectionConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String, jakarta.jms.ServerSessionPool, int)
	 */
	@Override
	public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName,
			String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
		LOGGER.trace(
				"createDurableConnectionConsumer(Topic: {}, String: {},	String: {}, ServerSessionPool: {}, int: {})",
				topic, subscriptionName, messageSelector, sessionPool, maxMessages);
		return jmsConnection.createDurableConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool,
				maxMessages);
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createSharedDurableConnectionConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String, jakarta.jms.ServerSessionPool, int)
	 */
	@Override
	public ConnectionConsumer createSharedDurableConnectionConsumer(Topic topic, String subscriptionName,
			String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException {
		LOGGER.trace(
				"createSharedDurableConnectionConsumer(Topic: {}, String: {},String: {}, ServerSessionPool: {}, int: {})",
				topic, subscriptionName, messageSelector, sessionPool, maxMessages);
		return jmsConnection.createSharedDurableConnectionConsumer(topic, subscriptionName, messageSelector,
				sessionPool, maxMessages);
	}
}
