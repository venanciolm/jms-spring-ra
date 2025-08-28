package com.farmafene.jms.outbound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.JMSException;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.LocalTransaction;
import jakarta.resource.spi.LocalTransactionException;

public class JMSLocalTransaction implements LocalTransaction {

	private static final Logger LOG = LoggerFactory.getLogger(JMSLocalTransaction.class);
	private JMSManagedSession jmsManagedConnection;

	public JMSLocalTransaction(JMSManagedSession jmsManagedConnection) {
		this.jmsManagedConnection = jmsManagedConnection;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("jmsManagedConnection=").append(jmsManagedConnection);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 
	 * @see jakarta.resource.spi.LocalTransaction#begin()
	 */
	@Override
	public void begin() throws ResourceException {
		LOG.trace("begin(), managed: {}", jmsManagedConnection);
	}

	/**
	 * 
	 * @see jakarta.resource.spi.LocalTransaction#commit()
	 */
	@Override
	public void commit() throws ResourceException {
		try {
			jmsManagedConnection.getHandler().getXASession().getSession().commit();
			LOG.trace("commit(), managed: {}", jmsManagedConnection);
		} catch (JMSException e) {
			LocalTransactionException lte = new LocalTransactionException(e);
			LOG.error("commit(), managed: {}", jmsManagedConnection, e);
			throw lte;
		}
	}

	/**
	 * 
	 * @see jakarta.resource.spi.LocalTransaction#rollback()
	 */
	@Override
	public void rollback() throws ResourceException {
		try {
			jmsManagedConnection.getHandler().getXASession().getSession().rollback();
			LOG.trace("rollback(), managed: {}", jmsManagedConnection);
		} catch (JMSException e) {
			LocalTransactionException lte = new LocalTransactionException(e);
			LOG.error("rollback(), managed: {}", jmsManagedConnection, lte);
			throw lte;
		}
	}
}
