package com.farmafene.jms.ra;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.resource.spi.UnavailableException;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;
import jakarta.resource.spi.work.WorkException;

/**
 * @see org.apache.activemq.artemis.ra.inflow.ActiveMQActivation
 */
public class JMSActivation {
	private static final Logger LOG = LoggerFactory.getLogger(JMSActivation.class);
	private JMSResourceAdapter ra;
	JMSActivationSpec spec;
	MessageEndpointFactory endpointFactory;
	private List<JMSInBoundXAResource> works;

	public JMSActivation(JMSResourceAdapter ra, JMSActivationSpec spec, MessageEndpointFactory endpointFactory) {
		this.ra = ra;
		this.spec = spec;
		this.endpointFactory = endpointFactory;
		this.works = new CopyOnWriteArrayList<JMSInBoundXAResource>();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("resourceAdapter=").append(ra);
		sb.append(", activationSpec=").append(spec);
		sb.append(", endpointFactory=").append(endpointFactory);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Realiza el arranque de la activación concreta de JMS.
	 * @throws UnavailableException
	 */
	public void start() throws UnavailableException {
		LOG.info("Inicializando con {} workers el elemento {}", spec.getNumSessions(), this);
		for (int i = 0; i < spec.getNumSessions(); i++) {
			try {
				JMSInBoundXAResource work = new JMSInBoundXAResource(this);
				works.add(work);
				ra.getBootstrapContext().getWorkManager().scheduleWork(work);
			} catch (WorkException e) {
				LOG.error("{}.start()", this, e);
			}
		}
	}

	/**
	 * Realiza la parada de la Activación concreta de JMS.
	 */
	public void stop() {
		for (JMSInBoundXAResource w : works) {
			w.stop();
		}
	}

	/**
	 * @return the endpointFactory
	 */
	public MessageEndpointFactory getEndpointFactory() {
		return endpointFactory;
	}
}
