package com.farmafene.jms.outbound;

import java.io.PrintWriter;
import java.util.Objects;
import java.util.Set;

import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.XAConnectionFactory;
import jakarta.resource.NotSupportedException;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionManager;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionFactory;
import jakarta.resource.spi.ManagedConnectionMetaData;
import jakarta.resource.spi.ResourceAllocationException;

@SuppressWarnings("serial")
public class JMSManagedConnectionFactory implements ManagedConnectionFactory,
		ConnectionFactory /* , XAQueueConnectionFactory, XATopicConnectionFactory */ {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSManagedConnectionFactory.class);
	private PrintWriter pf;
	private XAConnectionFactory xamcf;
	private ConnectionManager cManager;
	private JMSSessionHandlerFactory handlerFactory;
	private int numConnections = 4;
	private ConnectionRequestInfo cri = new ConnectionRequestInfo() {
	};
	private ManagedConnectionMetaData managedConnectionMetaData = new ManagedConnectionMetaData() {

		/**
		 * 
		 * @see jakarta.resource.spi.ManagedConnectionMetaData#getUserName()
		 */
		@Override
		public String getUserName() throws ResourceException {
			return "@see " + getObject(JMSManagedConnectionFactory.this.xamcf, XAConnectionFactory.class);
		}

		/**
		 * 
		 * @see jakarta.resource.spi.ManagedConnectionMetaData#getMaxConnections()
		 */
		@Override
		public int getMaxConnections() throws ResourceException {
			return JMSManagedConnectionFactory.this.numConnections;
		}

		/**
		 * 
		 * @see jakarta.resource.spi.ManagedConnectionMetaData#getEISProductVersion()
		 */
		@Override
		public String getEISProductVersion() throws ResourceException {
			return "1.0";
		}

		/**
		 * 
		 * @see jakarta.resource.spi.ManagedConnectionMetaData#getEISProductName()
		 */
		@Override
		public String getEISProductName() throws ResourceException {
			return "EIS Product Name Dummy";
		}
	};

	public JMSManagedConnectionFactory(XAConnectionFactory xamcf) {
		if (null == xamcf) {
			throw new IllegalArgumentException("debe establecerse un objeto no nulo!");
		}
		this.xamcf = xamcf;
		LOGGER.warn("Se a introducido: {}", this.xamcf);
		this.handlerFactory = new JMSSessionHandlerFactory(this, getObject(this.xamcf, XAConnectionFactory.class));
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("XAConnectionFactory=").append(xamcf);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(cManager, numConnections, xamcf);
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
		JMSManagedConnectionFactory other = (JMSManagedConnectionFactory) obj;
		return Objects.equals(cManager, other.cManager) && numConnections == other.numConnections
				&& Objects.equals(xamcf, other.xamcf);
	}

	<T> T getObject(Object target, Class<T> clazz) {
		if (!clazz.isAssignableFrom(target.getClass())) {

			UnsupportedOperationException uop = new UnsupportedOperationException(
					String.format("$1%s is not a %2$s", target.toString(), clazz.getCanonicalName()));
			LOGGER.error("Error en el procesado", uop);
			throw uop;
		}
		@SuppressWarnings("unchecked")
		T out = (T) target;
		return out;
	}

//
//----------------------------------------------------------------------------
//

	/**
	 * 
	 * @see jakarta.jms.ConnectionFactory#createConnection()
	 */
	@Override
	public Connection createConnection() throws JMSException {
		LOGGER.info("createConnection()");
		ConnectionFromJMSSessionHandler s = null;
		try {
			s = getObject(cManager.allocateConnection(this, cri), ConnectionFromJMSSessionHandler.class);
			LOGGER.info("Devolviendo la conexion: {}", s);

		} catch (ResourceException e) {
			jakarta.jms.ResourceAllocationException rac = new jakarta.jms.ResourceAllocationException("", "", e);
			LOGGER.error("Error en la recuperación de la conexión", rac);
			throw rac;
		} catch (ClassCastException e) {
			LOGGER.error("Error en cast", e);
			jakarta.jms.ResourceAllocationException rac = new jakarta.jms.ResourceAllocationException("", "", e);
			LOGGER.error("Error en la recuperación de la conexión", rac);
			throw rac;
		}
		return s;
	}

	/**
	 * 
	 * @see jakarta.jms.ConnectionFactory#createConnection(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Connection createConnection(String userName, String password) throws JMSException {
		return createConnection();
	}

	/**
	 * 
	 * @see jakarta.jms.ConnectionFactory#createContext()
	 */
	@Override
	public JMSContext createContext() {
		UnsupportedOperationException uop = new UnsupportedOperationException(
				String.format("%1$s not supported Yet!", "jakarta.jms.ConnectionFactory#createContext()"));
		LOGGER.error("Error en el procesado", uop);
		throw uop;
	}

	/**
	 * 
	 * @see jakarta.jms.ConnectionFactory#createContext(java.lang.String,java.lang.String)
	 */
	@Override
	public JMSContext createContext(String userName, String password) {
		UnsupportedOperationException uop = new UnsupportedOperationException(String.format("%1$s not supported Yet!",
				"jakarta.jms.ConnectionFactory#createContext(java.lang.String,java.lang.String)"));
		LOGGER.error("Error en el procesado", uop);
		throw uop;
	}

	/**
	 * 
	 * @see jakarta.jms.ConnectionFactory#createContext(java.lang.String,java.lang.String,
	 *      int)
	 */
	@Override
	public JMSContext createContext(String userName, String password, int sessionMode) {
		UnsupportedOperationException uop = new UnsupportedOperationException(String.format("%1$s not supported Yet!",
				"jakarta.jms.ConnectionFactory#createContext(java.lang.String,java.lang.String, int)"));
		LOGGER.error("Error en el procesado", uop);
		throw uop;
	}

	/**
	 * 
	 * @see jakarta.jms.ConnectionFactory#createContext(int)
	 */
	@Override
	public JMSContext createContext(int sessionMode) {
		UnsupportedOperationException uop = new UnsupportedOperationException(
				String.format("%1$s not supported Yet!", "jakarta.jms.ConnectionFactory#createContext(int)"));
		LOGGER.error("Error en el procesado", uop);
		throw uop;
	}

//
//----------------------------------------------------------------------------
//

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnectionFactory#createConnectionFactory(jakarta.resource.spi.ConnectionManager)
	 */
	@Override
	public Object createConnectionFactory(ConnectionManager cManager) throws ResourceException {
		this.cManager = cManager;
		return this;
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnectionFactory#createManagedConnection(javax.security.auth.Subject,
	 *      jakarta.resource.spi.ConnectionRequestInfo)
	 */
	@Override
	public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cxRequestInfo)
			throws ResourceException {
		try {
			JMSManagedSession mSess = null;
			JMSSessionHandler h = handlerFactory.getHandler();
			mSess = new JMSManagedSession(this, h);
			LOGGER.info("createManagedConnection(Subject: {}, ConnectionRequestInfo: {}):={}", subject, cxRequestInfo,
					mSess);
			return mSess;
		} catch (JMSException e) {
			throw new ResourceAllocationException(e);
		}
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnectionFactory#matchManagedConnections(java.util.Set,
	 *      javax.security.auth.Subject, jakarta.resource.spi.ConnectionRequestInfo)
	 */
	@Override
	public ManagedConnection matchManagedConnections(@SuppressWarnings("rawtypes") Set connectionSet, Subject subject,
			ConnectionRequestInfo cxRequestInfo) throws ResourceException {
		ManagedConnection mcOut = null;
		for (Object o : connectionSet) {
			if (o instanceof JMSManagedSession) {
				JMSManagedSession mc = (JMSManagedSession) o;
				if (mc.matches(this, subject, cxRequestInfo)) {
					mcOut = mc;
					break;
				}
			}
		}
		LOGGER.info("matchManagedConnections(Set: {}, Subject: {}, ConnectionRequestInfo: {}):=", connectionSet,
				subject, cxRequestInfo, mcOut);
		return mcOut;
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnectionFactory#createConnectionFactory()
	 */
	@Override
	public Object createConnectionFactory() throws ResourceException {
		throw new NotSupportedException();
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnectionFactory#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(PrintWriter out) throws ResourceException {
		this.pf = out;
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnectionFactory#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws ResourceException {
		return pf;
	}

	/**
	 * @return the numConnections
	 */
	public int getNumConnections() {
		return numConnections;
	}

	/**
	 * @param numConnections the numConnections to set
	 */
	public void setNumConnections(int numConnections) {
		this.numConnections = numConnections;
	}

	public ManagedConnectionMetaData getManagedConnectionMetaData() {
		return this.managedConnectionMetaData;
	}
}
