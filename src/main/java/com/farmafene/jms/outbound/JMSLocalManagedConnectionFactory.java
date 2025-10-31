package com.farmafene.jms.outbound;

import java.io.PrintWriter;
import java.util.Set;

import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.resource.NotSupportedException;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionManager;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionFactory;
import jakarta.resource.spi.ResourceAdapterInternalException;

@SuppressWarnings("serial")
public class JMSLocalManagedConnectionFactory implements ManagedConnectionFactory {

	private static Logger LOGGER = LoggerFactory.getLogger(JMSLocalManagedConnectionFactory.class);
	private PrintWriter logWriter;
	private ConnectionFactory cf;

	public JMSLocalManagedConnectionFactory(ConnectionFactory cf) {
		this.cf = cf;
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnectionFactory#createConnectionFactory(jakarta.resource.spi.ConnectionManager)
	 */
	@Override
	public JMSLocalConnectionFactory createConnectionFactory(ConnectionManager cxManager) throws ResourceException {
		LOGGER.trace("createConnectionFactory(ConnectionManager: {})", cxManager);
		return new JMSLocalConnectionFactory(this, cxManager);
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnectionFactory#createConnectionFactory()
	 */
	@Override
	public Object createConnectionFactory() throws ResourceException {
		throw new NotSupportedException("ConnectionManager is required");
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnectionFactory#createManagedConnection(javax.security.auth.Subject,
	 *      jakarta.resource.spi.ConnectionRequestInfo)
	 */
	@Override
	public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cxRequestInfo)
			throws ResourceException {
		Connection jmsConnection = getPhysicalConnection();
		ManagedJMSConnection mc = new ManagedJMSConnection(this, jmsConnection);
		LOGGER.trace("createManagedConnection(Subject: {}, ConnectionRequestInfo: {})::{}", subject, cxRequestInfo, mc);
		return mc;
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnectionFactory#matchManagedConnections(java.util.Set,
	 *      javax.security.auth.Subject, jakarta.resource.spi.ConnectionRequestInfo)
	 */
	@Override
	public ManagedConnection matchManagedConnections(@SuppressWarnings("rawtypes") Set set, Subject subject,
			ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
		LOGGER.trace("matchManagedConnections(Set: {},Subject: {},ConnectionRequestInfo: {})", set.size(), subject,
				connectionRequestInfo);
		for (Object o : set) {
			if (o instanceof ManagedJMSConnection) {
				ManagedJMSConnection mc = (ManagedJMSConnection) o;
				if (mc.matches(this, subject, connectionRequestInfo)) {
					return mc;
				}
			}
		}
		return null;
	}

	protected Connection getPhysicalConnection() throws ResourceException {
		try {
			return cf.createConnection();
		} catch (JMSException e) {
			throw new ResourceAdapterInternalException("Unable to obtain physical connection to " + cf, e);
		}
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnectionFactory#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(PrintWriter out) throws ResourceException {
		this.logWriter = out;

	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnectionFactory#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws ResourceException {
		return this.logWriter;
	}

}
