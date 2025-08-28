package com.farmafene.jms.outbound;

import java.util.Objects;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.JMSRuntimeException;
import jakarta.jms.XAConnection;
import jakarta.jms.XAConnectionFactory;
import jakarta.jms.XAJMSContext;

public class XAConnectionFactoryFromConnectionFactory implements XAConnectionFactory {
	private ConnectionFactory connectionFactory;

	public XAConnectionFactoryFromConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	/**
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("connectionFactory=").append(connectionFactory);
		sb.append("]");
		return sb.toString();
	}
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(connectionFactory);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XAConnectionFactoryFromConnectionFactory other = (XAConnectionFactoryFromConnectionFactory) obj;
		return Objects.equals(connectionFactory, other.connectionFactory);
	}

	/**
	 * 
	 * @see jakarta.jms.XAConnectionFactory#createXAConnection()
	 */
	@Override
	public XAConnection createXAConnection() throws JMSException {
		return new XAConnectionFromConnection(connectionFactory.createConnection(), connectionFactory);
	}

	/**
	 * 
	 * @see jakarta.jms.XAConnectionFactory#createXAConnection(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public XAConnection createXAConnection(String userName, String password) throws JMSException {
		return createXAConnection();
	}

	/**
	 * 
	 * @see jakarta.jms.XAConnectionFactory#createXAContext()
	 */
	@Override
	public XAJMSContext createXAContext() {
		throw new JMSRuntimeException("Not implemented! (" + this + ")");
	}

	/**
	 * 
	 * @see jakarta.jms.XAConnectionFactory#createXAContext(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public XAJMSContext createXAContext(String userName, String password) {
		throw new JMSRuntimeException("Not implemented! (" + this + ")");
	}
}
