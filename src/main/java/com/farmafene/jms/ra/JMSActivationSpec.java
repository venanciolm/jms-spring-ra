package com.farmafene.jms.ra;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.XAConnectionFactory;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.ActivationSpec;
import jakarta.resource.spi.InvalidPropertyException;
import jakarta.resource.spi.ResourceAdapter;

public class JMSActivationSpec implements ActivationSpec {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSActivationSpec.class);
	private ResourceAdapter resourceAdapter;
	private XAConnectionFactory xAConnectionFactory;
	private int numSessions = 2;
	private boolean localTransactions = true;
	private String queue;

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("resourceAdapter=").append(resourceAdapter);
		sb.append(", XAConnectionFactory=").append(xAConnectionFactory);
		sb.append(", queue=").append(queue);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(numSessions, xAConnectionFactory, queue);
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
		JMSActivationSpec other = (JMSActivationSpec) obj;
		return numSessions == other.numSessions //
				&& Objects.equals(queue, other.queue) //
				&& Objects.equals(xAConnectionFactory, other.xAConnectionFactory);
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ActivationSpec#validate()
	 */
	@Override
	public void validate() throws InvalidPropertyException {
		LOGGER.info("validate() '{}' ", this);
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ResourceAdapterAssociation#getResourceAdapter()
	 */
	@Override
	public ResourceAdapter getResourceAdapter() {
		LOGGER.debug("Devolviendo '{}' como resource adapter", this.resourceAdapter);
		return this.resourceAdapter;
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ResourceAdapterAssociation#setResourceAdapter(jakarta.resource.spi.ResourceAdapter)
	 */
	@Override
	public void setResourceAdapter(ResourceAdapter ra) throws ResourceException {
		LOGGER.debug("Estableciendo '{}' como resource adapter", ra);
		this.resourceAdapter = ra;
	}

	/**
	 * @return the xAConnectionFactory
	 */
	public XAConnectionFactory getXAConnectionFactory() {
		return xAConnectionFactory;
	}

	/**
	 * @param xAConnectionFactory the xAConnectionFactory to set
	 */
	public void setXAConnectionFactory(XAConnectionFactory xAConnectionFactory) {
		this.xAConnectionFactory = xAConnectionFactory;
	}

	/**
	 * @return the numSessions
	 */
	public int getNumSessions() {
		return numSessions;
	}

	/**
	 * @param numSessions the numSessions to set
	 */
	public void setNumSessions(int numSessions) {
		this.numSessions = numSessions;
	}

	/**
	 * @return the localTransactions
	 */
	public boolean isLocalTransactions() {
		return localTransactions;
	}

	/**
	 * @param localTransactions the localTransactions to set
	 */
	public void setLocalTransactions(boolean localTransactions) {
		this.localTransactions = localTransactions;
	}

	/**
	 * @param queue the queue to set
	 */
	public void setQueue(String queue) {
		this.queue = queue;
	}

	/**
	 * @return the queue
	 */
	public String getQueue() {
		return queue;
	}
}
