package com.farmafene.jms.outbound;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionConsumer;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.ConnectionMetaData;
import jakarta.jms.Destination;
import jakarta.jms.ExceptionListener;
import jakarta.jms.JMSException;
import jakarta.jms.ServerSessionPool;
import jakarta.jms.Session;
import jakarta.jms.Topic;
import jakarta.jms.XAConnection;
import jakarta.jms.XASession;

public class XAConnectionFromConnection implements XAConnection {

	private Connection conn;
	private ConnectionFactory connectionFactory;

	public XAConnectionFromConnection(Connection conn, ConnectionFactory connectionFactory) {
		this.conn = conn;
		this.connectionFactory = connectionFactory;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("connection=").append(conn);
		sb.append("]");
		return sb.toString();
	}

	//
	//
	// Métodos XA
	// @Override
	// public XASession createXASession() throws JMSException {
	// // TODO Auto-generated method stub
	// return null;
	// }
	/**
	 * Creates an {@code XASession} object.
	 *
	 * @return a newly created {@code XASession}
	 *
	 * @exception JMSException if the {@code XAConnection} object fails to create an
	 *                         {@code XASession} due to some internal error.
	 *
	 * @since JMS 1.1
	 */
	@Override
	public XASession createXASession() throws JMSException {
		return new XASessionFromSession(conn.createSession(true, Session.SESSION_TRANSACTED), this.connectionFactory);
	}

	/**
	 * Creates an {@code Session} object.
	 *
	 * @param transacted      usage undefined
	 * @param acknowledgeMode usage undefined
	 *
	 * @return a newly created {@code Session}
	 *
	 * @exception JMSException if the {@code XAConnection} object fails to create a
	 *                         {@code Session} due to some internal error.
	 *
	 * @since JMS 1.1
	 */
	@Override
	public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
		Session sess = conn.createSession(transacted, acknowledgeMode);
		XASession xASess = new XASessionFromSession(sess, this.connectionFactory);
		return xASess;
	}

	// --
	/**
	 * 
	 * @see jakarta.jms.Connection#start()
	 */
	@Override
	public void start() throws JMSException {
		this.conn.start();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#stop()
	 */
	@Override
	public void stop() throws JMSException {
		this.conn.stop();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#close()
	 */
	@Override
	public void close() throws JMSException {
		this.conn.close();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#getClientID()
	 */
	@Override
	public String getClientID() throws JMSException {
		return this.conn.getClientID();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#setClientID(java.lang.String)
	 */
	@Override
	public void setClientID(String clientID) throws JMSException {
		this.conn.setClientID(clientID);
	}

	// --
	/**
	 * 
	 * @see jakarta.jms.Connection#createSession(int)
	 */
	@Override
	public Session createSession(int sessionMode) throws JMSException {
		Session sess = conn.createSession(sessionMode);
		XASession xASess = new XASessionFromSession(sess, this.connectionFactory);
		return xASess;
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createSession()
	 */
	@Override
	public Session createSession() throws JMSException {
		Session sess = conn.createSession();
		XASession xASess = new XASessionFromSession(sess, this.connectionFactory);
		return xASess;
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#getMetaData()
	 */
	@Override
	public ConnectionMetaData getMetaData() throws JMSException {
		return conn.getMetaData();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#getExceptionListener()
	 */
	@Override
	public ExceptionListener getExceptionListener() throws JMSException {
		return conn.getExceptionListener();
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#setExceptionListener(jakarta.jms.ExceptionListener)
	 */
	@Override
	public void setExceptionListener(ExceptionListener listener) throws JMSException {
		this.conn.setExceptionListener(listener);
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createConnectionConsumer(jakarta.jms.Destination,
	 *      java.lang.String, jakarta.jms.ServerSessionPool, int)
	 */
	@Override
	public ConnectionConsumer createConnectionConsumer(//
			Destination destination, //
			String messageSelector, //
			ServerSessionPool sessionPool, //
			int maxMessages //
	) throws JMSException {
		return conn.createConnectionConsumer(destination, messageSelector, sessionPool, maxMessages);
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createSharedConnectionConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String, jakarta.jms.ServerSessionPool, int)
	 */
	@Override
	public ConnectionConsumer createSharedConnectionConsumer(//
			Topic topic, //
			String subscriptionName, //
			String messageSelector, //
			ServerSessionPool sessionPool, //
			int maxMessages //
	) throws JMSException {
		return conn.createSharedConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool, maxMessages);
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createDurableConnectionConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String, jakarta.jms.ServerSessionPool, int)
	 */
	@Override
	public ConnectionConsumer createDurableConnectionConsumer(//
			Topic topic, //
			String subscriptionName, //
			String messageSelector, //
			ServerSessionPool sessionPool, //
			int maxMessages //
	) throws JMSException {
		return conn.createDurableConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool, maxMessages);
	}

	/**
	 * 
	 * @see jakarta.jms.Connection#createSharedDurableConnectionConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String, jakarta.jms.ServerSessionPool, int)
	 */
	@Override
	public ConnectionConsumer createSharedDurableConnectionConsumer( //
			Topic topic, //
			String subscriptionName, //
			String messageSelector, //
			ServerSessionPool sessionPool, //
			int maxMessages //
	) throws JMSException {
		return conn.createSharedDurableConnectionConsumer(topic, subscriptionName, messageSelector, sessionPool,
				maxMessages);
	}
}
