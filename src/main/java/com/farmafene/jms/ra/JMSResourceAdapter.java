package com.farmafene.jms.ra;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ActivationSpec;
import jakarta.resource.spi.BootstrapContext;
import jakarta.resource.spi.ResourceAdapter;
import jakarta.resource.spi.ResourceAdapterInternalException;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;

public class JMSResourceAdapter implements ResourceAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSResourceAdapter.class);
	private BootstrapContext bootstrapContext;
	private Map<JMSActivationSpec, JMSActivation> activationSpecs;

	public JMSResourceAdapter() {
		this.activationSpecs = new ConcurrentHashMap<JMSActivationSpec, JMSActivation>();
	}

	/**
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("specs=").append(this.activationSpecs.size());
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activationSpecs == null) ? 0 : activationSpecs.hashCode());
		return result;
	}

	/**
	 * 
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
		JMSResourceAdapter other = (JMSResourceAdapter) obj;
		if (activationSpecs == null) {
			if (other.activationSpecs != null)
				return false;
		} else if (!activationSpecs.equals(other.activationSpecs))
			return false;
		return true;
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ResourceAdapter#start(jakarta.resource.spi.BootstrapContext)
	 */
	@Override
	public void start(BootstrapContext ctx) throws ResourceAdapterInternalException {
		this.bootstrapContext = ctx;
		LOGGER.info("start(BootstrapContext={})", ctx);
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ResourceAdapter#stop()
	 */
	@Override
	public void stop() {
		LOGGER.info("stop()");
		JMSActivationSpec[] lista = activationSpecs.keySet().toArray(new JMSActivationSpec[0]);
		for (JMSActivationSpec s : lista) {
			this.endpointDeactivation(activationSpecs.get(s).getEndpointFactory(), s);
		}
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ResourceAdapter#endpointActivation(jakarta.resource.spi.endpoint.MessageEndpointFactory,
	 *      jakarta.resource.spi.ActivationSpec)
	 */
	@Override
	public void endpointActivation(MessageEndpointFactory endpointFactory, ActivationSpec spec)
			throws ResourceException {
		if (spec instanceof JMSActivationSpec) {
			JMSActivationSpec as = (JMSActivationSpec) spec;
			JMSActivation activation = new JMSActivation(this, as, endpointFactory);
			activationSpecs.put((JMSActivationSpec) spec, activation);
			LOGGER.info("endpointActivation(MessageEndpointFactory={}, ActivationSpec={})", endpointFactory, spec);
			activation.start();
		} else {
			IllegalStateException e = new IllegalStateException("Imposible activar en este ResourceAdapter");
			LOGGER.error("Error en endpointActivation(MessageEndpointFactory={}, ActivationSpec={})", endpointFactory,
					spec, e);
			throw e;
		}
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ResourceAdapter#endpointDeactivation(jakarta.resource.spi.endpoint.MessageEndpointFactory,
	 *      jakarta.resource.spi.ActivationSpec)
	 */
	@Override
	public void endpointDeactivation(MessageEndpointFactory endpointFactory, ActivationSpec spec) {
		LOGGER.info("endpointDeactivation(MessageEndpointFactory={}, ActivationSpec={})", endpointFactory, spec);
		JMSActivation activation = activationSpecs.remove(spec);
		if (null != activation) {
			activation.stop();
		}
	}

	/**
	 * 
	 * @see jakarta.resource.spi.ResourceAdapter#getXAResources(jakarta.resource.spi.ActivationSpec[])
	 */
	@Override
	public XAResource[] getXAResources(ActivationSpec[] specs) throws ResourceException {
		LOGGER.warn("getXAResources(ActivationSpec[]={})", Arrays.toString(specs));
		// Solo para recovery. No implementado.
		return new XAResource[0];
	}

	/**
	 * @return the bootstrapContext
	 */
	public BootstrapContext getBootstrapContext() {
		return bootstrapContext;
	}
}
