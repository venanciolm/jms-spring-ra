package com.farmafene.jms.outbound;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.ExceptionListener;
import jakarta.jms.JMSException;
import jakarta.jms.XAConnection;

class JMSConnectionHandler implements ExceptionListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(JMSConnectionHandler.class);
	private XAConnection connection;
	private List<JMSSessionHandler> handlers;
	private String id;

	JMSConnectionHandler(XAConnection connection) {
		this.connection = connection;
		handlers = new CopyOnWriteArrayList<JMSSessionHandler>();
		this.id = UUID.randomUUID().toString();
	}

	public void start() throws JMSException {
		this.connection.setExceptionListener(this);
	}

	/**
	 * 
	 * @see jakarta.jms.ExceptionListener#onException(jakarta.jms.JMSException)
	 */
	@Override
	public void onException(JMSException exception) {
		if (LOGGER.isErrorEnabled()) {
			String lf = System.lineSeparator();
			StringBuilder sb = new StringBuilder();
			sb.append(lf).append("/+------------------------------+");
			sb.append(lf).append(" | Error en la conexion         |");
			sb.append(lf).append(" +------------------------------+");
			sb.append(lf).append(" | id:  ").append(this.id);
			sb.append(lf).append(" | con: ").append(this.connection);
			sb.append(lf).append(" +------------------------------+");
			sb.append(lf);
			LOGGER.error("{}", sb, exception);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("id=").append(id);
		sb.append(", connection=").append(connection);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id);
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
		JMSConnectionHandler other = (JMSConnectionHandler) obj;
		return Objects.equals(id, other.id);
	}

	/**
	 * @return the handlers
	 */
	public List<JMSSessionHandler> getHandlers() {
		return handlers;
	}

	/**
	 * @param handlers the handlers to set
	 */
	public void setHandlers(List<JMSSessionHandler> handlers) {
		this.handlers = handlers;
	}

	/**
	 * @return the connection
	 */
	public XAConnection getConnection() {
		return connection;
	}
}
