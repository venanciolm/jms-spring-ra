package com.farmafene.jms.outbound;

import java.util.Objects;
import java.util.UUID;

import jakarta.jms.XASession;

public class JMSSessionHandler {

	private JMSConnectionHandler jmsConnectionHandler;
	private XASession xASession;
	private String id;

	public JMSSessionHandler(JMSConnectionHandler ch, XASession xaS) {
		id = UUID.randomUUID().toString();
		this.jmsConnectionHandler = ch;
		this.xASession = xaS;
		ch.getHandlers().add(this);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("id=").append(id);
		sb.append(":").append(jmsConnectionHandler);
		sb.append(", xASession=").append(xASession);
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
		JMSSessionHandler other = (JMSSessionHandler) obj;
		return Objects.equals(id, other.id);
	}

	/**
	 * @return the jmsConnectionHandler
	 */
	public JMSConnectionHandler getJmsConnectionHandler() {
		return jmsConnectionHandler;
	}

	/**
	 * @return the xASession
	 */
	public XASession getXASession() {
		return xASession;
	}

}
