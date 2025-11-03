package com.farmafene.jms.outbound;

import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.Connection;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import jakarta.resource.NotSupportedException;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionEvent;
import jakarta.resource.spi.ConnectionEventListener;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.LocalTransaction;
import jakarta.resource.spi.LocalTransactionException;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionMetaData;

public class ManagedJMSConnection implements ManagedConnection {
	private static final Logger LOGGER = LoggerFactory.getLogger(ManagedJMSConnection.class);

	private ManagedJMSConnectionLocalTx localTx;
	private Session session;
	private Connection jmsConnection;
	private JMSLocalManagedConnectionFactory jmsLocalManagedConnectionFactory;
	private List<ConnectionEventListener> connectionEventListeners;
	private String id;
	private PrintWriter logWriter;
	private ManagedConnectionMetaData metatata;

	public static class ManagedJMSConnectionLocalTx implements LocalTransaction {

		private ManagedJMSConnection managedJMSConnection = null;

		private ManagedJMSConnectionLocalTx(ManagedJMSConnection managedJMSConnection) {
			this.managedJMSConnection = managedJMSConnection;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(getClass().getSimpleName()).append(" [");
			sb.append("jmsManagedConnection=").append(managedJMSConnection);
			sb.append("]");
			return sb.toString();
		}

		/**
		 * 
		 * @see jakarta.resource.spi.LocalTransaction#begin()
		 */
		@Override
		public void begin() throws ResourceException {
			LOGGER.trace("begin(), managed: {}", managedJMSConnection);
			managedJMSConnection.localTransactionStarted();
		}

		/**
		 * 
		 * @see jakarta.resource.spi.LocalTransaction#commit()
		 */
		@Override
		public void commit() throws ResourceException {
			try {
				managedJMSConnection.session.commit();
				managedJMSConnection.localTransactionCommitted();
				LOGGER.trace("commit(), managed: {}", managedJMSConnection);
			} catch (JMSException e) {
				LocalTransactionException lte = new LocalTransactionException(e);
				LOGGER.error("commit(), managed: {}", managedJMSConnection, e);
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
				managedJMSConnection.session.rollback();
				managedJMSConnection.localTransactionRolledback();
				LOGGER.trace("rollback(), managed: {}", managedJMSConnection);
			} catch (JMSException e) {
				LocalTransactionException lte = new LocalTransactionException(e);
				LOGGER.error("rollback(), managed: {}", managedJMSConnection, lte);
				throw lte;
			}
		}
	}

	public ManagedJMSConnection(JMSLocalManagedConnectionFactory jmsLocalManagedConnectionFactory,
			Connection jmsConnection) {
		this.localTx = new ManagedJMSConnectionLocalTx(this);
		this.metatata = new ManagedConnectionMetaData() {

			/**
			 * 
			 * @return
			 * @throws ResourceException
			 */
			@Override
			public String getEISProductName() throws ResourceException {
				return "";
			}

			@Override
			public String getEISProductVersion() throws ResourceException {
				return "";
			}

			@Override
			public int getMaxConnections() throws ResourceException {
				return -1;
			}

			@Override
			public String getUserName() throws ResourceException {
				return "";
			}

		};
		this.jmsConnection = jmsConnection;
		this.jmsLocalManagedConnectionFactory = jmsLocalManagedConnectionFactory;
		this.connectionEventListeners = new CopyOnWriteArrayList<ConnectionEventListener>();
		this.id=UUID.randomUUID().toString();
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("=[");
		sb.append("id=").append(id);
		sb.append(", con=").append(jmsConnection);
		sb.append(", sess=").append(session);
		sb.append("]");
		return sb.toString();
	}

	public boolean matches(JMSLocalManagedConnectionFactory jmsLocalManagedConnectionFactory, Subject subject,
			ConnectionRequestInfo connectionRequestInfo) {
		LOGGER.trace("matches(JMSLocalManagedConnectionFactory: {}, Subject: {},	ConnectionRequestInfo:{})",
				jmsLocalManagedConnectionFactory, subject, connectionRequestInfo);
		return jmsLocalManagedConnectionFactory == this.jmsLocalManagedConnectionFactory;
	}

	public void connectionClosed(LocalConnection con) {
		LOGGER.trace("connectionClosed(LocalConnection: {})", con);
		ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.CONNECTION_CLOSED);
		event.setConnectionHandle(con);
		connectionEventListeners.forEach(//
				(l) -> {
					l.connectionClosed(event);
				} //
		);
	}

	public void connectionErrorOccurred(LocalConnection con) {
		LOGGER.trace("connectionClosed(LocalConnection: {})", con);
		ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.CONNECTION_ERROR_OCCURRED);
		event.setConnectionHandle(con);
		connectionEventListeners.forEach(//
				(l) -> {
					l.connectionErrorOccurred(event);
				} //
		);
	}

	public void localTransactionStarted() {
		LOGGER.trace("localTransactionStarted()");
		ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.LOCAL_TRANSACTION_STARTED);
		connectionEventListeners.forEach(//
				(l) -> {
					l.localTransactionStarted(event);
				} //
		);
	}

	public void localTransactionCommitted() {
		LOGGER.trace("localTransactionCommitted()");
		ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.LOCAL_TRANSACTION_COMMITTED);
		connectionEventListeners.forEach(//
				(l) -> {
					l.localTransactionCommitted(event);
				} //
		);
	}

	public void localTransactionRolledback() {
		LOGGER.trace("localTransactionRolledback()");
		ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.LOCAL_TRANSACTION_ROLLEDBACK);
		connectionEventListeners.forEach(//
				(l) -> {
					l.localTransactionRolledback(event);
				} //
		);
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#getConnection(javax.security.auth.Subject,
	 *      jakarta.resource.spi.ConnectionRequestInfo)
	 */
	@Override
	public Object getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
		return new LocalConnection(this, jmsConnection);
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#destroy()
	 */
	@Override
	public void destroy() throws ResourceException {
		LOGGER.trace("destroy()");
		session = null;
		try {
			jmsConnection.close();
		} catch (JMSException e) {
			LOGGER.warn("destroy()", e);
		}

	}

	@Override
	public void cleanup() throws ResourceException {
		LOGGER.trace("cleanup()");
		this.session = null;

	}

	@Override
	public void associateConnection(Object connection) throws ResourceException {
		LOGGER.trace("associateConnection(Object: {})", connection);
	}

	@Override
	public void addConnectionEventListener(ConnectionEventListener listener) {
		LOGGER.trace("addConnectionEventListener(ConnectionEventListener: {})", listener);
		this.connectionEventListeners.add(listener);
	}

	@Override
	public void removeConnectionEventListener(ConnectionEventListener listener) {
		this.connectionEventListeners.remove(listener);
	}

	@Override
	public XAResource getXAResource() throws ResourceException {
		throw new NotSupportedException("XA No soportado");
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#getLocalTransaction()
	 */
	@Override
	public LocalTransaction getLocalTransaction() throws ResourceException {
		return localTx;
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#getMetaData()
	 */
	@Override
	public ManagedConnectionMetaData getMetaData() throws ResourceException {
		LOGGER.trace("getMetadata()");
		return this.metatata;
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(PrintWriter out) throws ResourceException {
		LOGGER.trace("setLogWriter(PrintWriter: {})", out);
		this.logWriter = out;

	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws ResourceException {
		return this.logWriter;
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
}
