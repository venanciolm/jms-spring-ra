package com.farmafene.jms.outbound;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.JMSRuntimeException;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionManager;

public class JMSLocalConnectionFactory implements ConnectionFactory {

	private JMSLocalManagedConnectionFactory mcf;
	private ConnectionManager cm;

	public JMSLocalConnectionFactory(JMSLocalManagedConnectionFactory mcf, ConnectionManager cxManager) {
		this.mcf = mcf;
		this.cm = cxManager;
	}

	/**
	 * 
	 * @see jakarta.jms.ConnectionFactory#createConnection()
	 */
	@Override
	public Connection createConnection() throws JMSException {
		try {
			return (Connection) cm.allocateConnection(mcf, null);
		} catch (ResourceException e) {
			if (e.getCause() instanceof JMSException) {
				throw (JMSException) e.getCause();
			} else {
				JMSException jmse = new JMSException(e.getMessage(), "INIT");
				jmse.initCause(e);
				throw jmse;
			}
		}
	}

	/**
	 * 
	 * @see jakarta.jms.ConnectionFactory#createConnection(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Connection createConnection(String userName, String password) throws JMSException {
		return this.createConnection();
	}

	/**
	 * 
	 * @see jakarta.jms.ConnectionFactory#createContext()
	 */
	@Override
	public JMSContext createContext() {
		throw new JMSRuntimeException("Not implemented! (" + this + ")");
	}

	/**
	 * 
	 * @see jakarta.jms.ConnectionFactory#createContext(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public JMSContext createContext(String userName, String password) {
		throw new JMSRuntimeException("Not implemented! (" + this + ")");
	}

	/**
	 * 
	 * @see jakarta.jms.ConnectionFactory#createContext(java.lang.String,
	 *      java.lang.String, int)
	 */
	@Override
	public JMSContext createContext(String userName, String password, int sessionMode) {
		throw new JMSRuntimeException("Not implemented! (" + this + ")");
	}

	/**
	 * 
	 * @see jakarta.jms.ConnectionFactory#createContext(int)
	 */
	@Override
	public JMSContext createContext(int sessionMode) {
		throw new JMSRuntimeException("Not implemented! (" + this + ")");
	}
}
