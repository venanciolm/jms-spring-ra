package com.farmafene.jms.ra;

import java.lang.reflect.Method;

import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.MessageListener;
import jakarta.resource.spi.UnavailableException;
import jakarta.resource.spi.endpoint.MessageEndpoint;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;
import jakarta.transaction.TransactionManager;

public class JMSMessageEndpointFactory implements MessageEndpointFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSMessageEndpointFactory.class);

	private MessageListener endpoint;
	private TransactionManager transactionManager;
	private JMSActivationSpec activationSpec;

	public JMSMessageEndpointFactory(//
			TransactionManager transactionManager, //
			MessageListener listener //
	) {
		this.endpoint = listener;
		this.transactionManager = transactionManager;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("transactionManager=").append(transactionManager);
		sb.append(", endpoint=").append(endpoint);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 
	 * @see jakarta.resource.spi.endpoint.MessageEndpointFactory#createEndpoint(javax.transaction.xa.XAResource)
	 */
	@Override
	public MessageEndpoint createEndpoint(XAResource xaResource) throws UnavailableException {
		MessageEndpoint mep = new JMSMessageEndPoint(endpoint, transactionManager, xaResource);
		LOGGER.trace("createEndpoint(XAResource: {}):={}", xaResource, mep);
		return mep;
	}

	/**
	 * 
	 * @see jakarta.resource.spi.endpoint.MessageEndpointFactory#createEndpoint(javax.transaction.xa.XAResource,
	 *      long)
	 */
	@Override
	public MessageEndpoint createEndpoint(XAResource xaResource, long timeout) throws UnavailableException {
		MessageEndpoint mep = createEndpoint(xaResource);
		LOGGER.trace("createEndpoint(timeout: {}, XAResource: {}):={}", timeout, xaResource, mep);
		return mep;
	}

	/**
	 * 
	 * @see jakarta.resource.spi.endpoint.MessageEndpointFactory#isDeliveryTransacted(java.lang.reflect.Method)
	 */
	@Override
	public boolean isDeliveryTransacted(Method method) throws NoSuchMethodException {
		boolean out = true;
		LOGGER.trace("isDeliveryTransacted(method: {}):={}", method, out);
		return out;
	}

	/**
	 * 
	 * @see jakarta.resource.spi.endpoint.MessageEndpointFactory#getActivationName()
	 */
	@Override
	public String getActivationName() {
		String out = activationSpec == null ? "<null ActivationName>" : activationSpec.toString();
		LOGGER.trace("getActivationName():={}", out);
		return out;
	}

	/**
	 * 
	 * @see jakarta.resource.spi.endpoint.MessageEndpointFactory#getEndpointClass()
	 */
	@Override
	public Class<?> getEndpointClass() {
		Class<?> out = endpoint == null ? null : endpoint.getClass();
		LOGGER.trace("getEndpointClass():={}", out);
		return out;
	}

	/**
	 * @return the endpoint
	 */
	public MessageListener getEndpoint() {
		return endpoint;
	}

	/**
	 * @param endpoint the endpoint to set
	 */
	public void setEndpoint(MessageListener endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * @return the activationSpec
	 */
	public JMSActivationSpec getActivationSpec() {
		return activationSpec;
	}

	/**
	 * @param activationSpec the activationSpec to set
	 */
	public void setActivationSpec(JMSActivationSpec activationSpec) {
		this.activationSpec = activationSpec;
	}
}
