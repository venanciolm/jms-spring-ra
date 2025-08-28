package com.farmafene.jms.outbound;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.XASession;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionEvent;
import jakarta.resource.spi.ConnectionEventListener;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.LocalTransaction;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionMetaData;

public class JMSManagedSession implements ManagedConnection /*, DissociatableManagedConnection*/ {

	private JMSManagedConnectionFactory mcf;
	private List<ConnectionEventListener> listeners;
	private JMSSessionHandler handler;
	private static final Logger LOGGER = LoggerFactory.getLogger(JMSManagedSession.class);

	public JMSManagedSession(JMSManagedConnectionFactory mcf, JMSSessionHandler h) {
		this.listeners = new LinkedList<ConnectionEventListener>();
		this.mcf = mcf;
		this.handler = h;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("JMSSessionHandler=").append(handler);
		sb.append("]");
		return sb.toString();
	}

	XASession getPhisicalXASession() {
		return handler.getXASession();
	}

	public boolean matches(JMSManagedConnectionFactory mcf, Subject subject, ConnectionRequestInfo cxRequestInfo) {
		return this.mcf == mcf;
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#getConnection(javax.security.auth.Subject,
	 *      jakarta.resource.spi.ConnectionRequestInfo)
	 */
	@Override
	public Object getConnection(Subject subject, ConnectionRequestInfo cxRequestInfo) throws ResourceException {
		// devuelve un manejador del esta conexión.
		// pasamos de momento.
		LOGGER.trace("getConnection(subject: {}, cxRequestInfo: {}):={}", subject, cxRequestInfo, this);
		return new ConnectionFromJMSSessionHandler(handler);
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#destroy()
	 */
	@Override
	public void destroy() throws ResourceException {
		LOGGER.trace("destroy(), connection: {}", this);
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#cleanup()
	 */
	@Override
	public void cleanup() throws ResourceException {
		LOGGER.trace("cleanup(), connection: {}", this);
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#associateConnection(java.lang.Object)
	 */
	@Override
	public void associateConnection(Object connection) throws ResourceException {
		LOGGER.warn("associateConnection(Object: {}), connection: {}", connection, this);
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 * @see jakarta.resource.spi.DissociatableManagedConnection#dissociateConnections()
	 */
	 // @Override
	public void dissociateConnections() throws ResourceException {
		LOGGER.warn("dissociateConnections(), connection: {}", this);
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#addConnectionEventListener(jakarta.resource.spi.ConnectionEventListener)
	 */
	@Override
	public void addConnectionEventListener(ConnectionEventListener listener) {
		LOGGER.trace("addConnectionEventListener(ConnectionEventListener: {}), connection: {}", listener, this);
		this.listeners.add(listener);
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#removeConnectionEventListener(jakarta.resource.spi.ConnectionEventListener)
	 */
	@Override
	public void removeConnectionEventListener(ConnectionEventListener listener) {
		LOGGER.trace("removeConnectionEventListener(ConnectionEventListener: {}), connection: {}", listener, this);
		this.listeners.remove(listener);
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#getXAResource()
	 */
	@Override
	public XAResource getXAResource() throws ResourceException {
		LOGGER.info("getXAResource(), connection: {}", this);
		return this.handler.getXASession().getXAResource();
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#getLocalTransaction()
	 */
	@Override
	public LocalTransaction getLocalTransaction() throws ResourceException {
		LOGGER.info("getLocalTransaction(), connection: {}", this);
		return new JMSLocalTransaction(this);
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#getMetaData()
	 */
	@Override
	public ManagedConnectionMetaData getMetaData() throws ResourceException {
		return this.mcf.getManagedConnectionMetaData();
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(PrintWriter out) throws ResourceException {
		mcf.setLogWriter(out);
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ManagedConnection#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws ResourceException {
		return mcf.getLogWriter();
	}

	/**
	 * @return the handler
	 */
	public JMSSessionHandler getHandler() {
		return handler;
	}

	/**
	 * @see ConnectionEvent#CONNECTION_CLOSED
	 */
	public void connectionClosed() {
		ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.CONNECTION_CLOSED);
		for (ConnectionEventListener l : listeners) {
			l.localTransactionStarted(event);
		}
	}

	/**
	 * @see ConnectionEvent#LOCAL_TRANSACTION_STARTED
	 */
	public void localTransactionStarted() {
		ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.LOCAL_TRANSACTION_COMMITTED);
		for (ConnectionEventListener l : listeners) {
			l.localTransactionStarted(event);
		}
	}

	/**
	 * @see ConnectionEvent#LOCAL_TRANSACTION_COMMITTED
	 */
	public void localTransactionCommitted() {
		ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.LOCAL_TRANSACTION_COMMITTED);
		for (ConnectionEventListener l : listeners) {
			l.localTransactionRolledback(event);
		}
	}

	/**
	 * @see ConnectionEvent#LOCAL_TRANSACTION_ROLLEDBACK
	 */
	public void localTransactionRolledback() {
		ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.LOCAL_TRANSACTION_ROLLEDBACK);
		for (ConnectionEventListener l : listeners) {
			l.localTransactionRolledback(event);
		}

	}

	/**
	 * @see ConnectionEvent#CONNECTION_ERROR_OCCURRED
	 * @param e
	 */
	public void connectionErrorOccurred(Exception e) {
		ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.CONNECTION_ERROR_OCCURRED, e);
		for (ConnectionEventListener l : listeners) {
			l.connectionErrorOccurred(event);
		}
	}
}
