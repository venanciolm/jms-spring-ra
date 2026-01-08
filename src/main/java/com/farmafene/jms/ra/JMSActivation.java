package com.farmafene.jms.ra;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.commons.tx.XAHelper;

import jakarta.jms.Destination;
import jakarta.jms.ExceptionListener;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import jakarta.jms.XAConnection;
import jakarta.jms.XASession;
import jakarta.resource.spi.UnavailableException;
import jakarta.resource.spi.endpoint.MessageEndpoint;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;
import jakarta.resource.spi.work.Work;
import jakarta.resource.spi.work.WorkException;

/**
 * @see org.apache.activemq.artemis.ra.inflow.ActiveMQActivation
 */
public class JMSActivation {

	private static final int RETRY_MS = 60000;
	private static final Logger LOG = LoggerFactory.getLogger(JMSActivation.class);
	private JMSResourceAdapter ra;
	private JMSActivationSpec spec;
	private MessageEndpointFactory endpointFactory;
	private List<ConsumerWork> works;

	private static class XAResourceWrapper implements XAResource {

		private XAResource internal;

		XAResourceWrapper(XAResource internal) {
			setXAResource(internal);
		}

		public void setXAResource(XAResource xaResource) {
			LOG.info("Establecido el XAResouce en: {}", xaResource);
			this.internal = xaResource;
		}

		/**
		 * 
		 * @see javax.transaction.xa.XAResource#commit(javax.transaction.xa.Xid,
		 *      boolean)
		 */
		@Override
		public void commit(Xid xid, boolean onePhase) throws XAException {
			if (LOG.isTraceEnabled()) {
				LOG.trace("commit(onePhase: {}, Xid: {}), wrapped: {}", onePhase, xid, internal);
			}
			this.internal.commit(xid, onePhase);
		}

		/**
		 * 
		 * @see javax.transaction.xa.XAResource#end(javax.transaction.xa.Xid, int)
		 */
		@Override
		public void end(Xid xid, int flags) throws XAException {
			if (LOG.isTraceEnabled()) {
				LOG.trace("end(flags: {}, Xid: {}), wrapped: {}", XAHelper.getStringFromFlag(flags), xid, internal);
			}
			this.internal.end(xid, flags);
		}

		/**
		 * 
		 * @see javax.transaction.xa.XAResource#forget(javax.transaction.xa.Xid)
		 */
		@Override
		public void forget(Xid xid) throws XAException {
			if (LOG.isTraceEnabled()) {
				LOG.trace("forget(Xid: {}), wrapped: {}", xid, internal);
			}
			this.internal.forget(xid);
		}

		/**
		 * 
		 * @see javax.transaction.xa.XAResource#getTransactionTimeout()
		 */
		@Override
		public int getTransactionTimeout() throws XAException {
			int transactionTimeout = this.internal.getTransactionTimeout();
			if (LOG.isTraceEnabled()) {
				LOG.trace("getTransactionTimeout():={}, wrapped: {}", transactionTimeout, internal);
			}
			return transactionTimeout;

		}

		/**
		 * 
		 * @see javax.transaction.xa.XAResource#isSameRM(javax.transaction.xa.XAResource)
		 */
		@Override
		public boolean isSameRM(XAResource xares) throws XAException {
			boolean isSameRM = this.internal.isSameRM(xares);
			if (LOG.isTraceEnabled()) {
				LOG.trace("isSameRM(XAResource: {}):={}, wrapped: {}", xares, isSameRM, internal);
			}
			return isSameRM;
		}

		/**
		 * 
		 * @see javax.transaction.xa.XAResource#prepare(javax.transaction.xa.Xid)
		 */
		@Override
		public int prepare(Xid xid) throws XAException {
			if (LOG.isTraceEnabled()) {
				LOG.trace("prepare(Xid: {}), wrapped: {}", xid, internal);
			}
			return this.internal.prepare(xid);
		}

		/**
		 * 
		 * @see javax.transaction.xa.XAResource#recover(int)
		 */
		@Override
		public Xid[] recover(int flag) throws XAException {
			if (LOG.isTraceEnabled()) {
				LOG.trace("revover(flags: {}), wrapped: {}", XAHelper.getStringFromFlag(flag), internal);
			}
			return this.internal.recover(flag);
		}

		/**
		 * 
		 * @see javax.transaction.xa.XAResource#rollback(javax.transaction.xa.Xid)
		 */
		@Override
		public void rollback(Xid xid) throws XAException {
			if (LOG.isTraceEnabled()) {
				LOG.trace("rollback(Xid: {}), wrapped: {}", xid, internal);
			}
			this.internal.rollback(xid);
		}

		/**
		 * 
		 * @see javax.transaction.xa.XAResource#setTransactionTimeout(int)
		 */
		@Override
		public boolean setTransactionTimeout(int seconds) throws XAException {
			boolean out = this.internal.setTransactionTimeout(seconds);
			if (LOG.isTraceEnabled()) {
				LOG.trace("setTransactionTimeout(seconds: {}):={}, wrapped: {}", seconds, out, internal);
			}
			return out;
		}

		/**
		 * 
		 * @see javax.transaction.xa.XAResource#start(javax.transaction.xa.Xid, int)
		 */
		@Override
		public void start(Xid xid, int flags) throws XAException {
			if (LOG.isTraceEnabled()) {
				LOG.trace("start(flags: {}, Xid: {}), wrapped: {}", XAHelper.getStringFromFlag(flags), xid, internal);
			}
			this.internal.start(xid, flags);
		}

	}

	public JMSActivation(JMSResourceAdapter ra, JMSActivationSpec spec, MessageEndpointFactory endpointFactory) {
		this.ra = ra;
		this.spec = spec;
		this.endpointFactory = endpointFactory;
		this.works = new CopyOnWriteArrayList<JMSActivation.ConsumerWork>();
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

	public void start() {
		LOG.info("Inicializando con {} workers el elemento {}", spec.getNumSessions(), this);
		for (int i = 0; i < spec.getNumSessions(); i++) {
			try {
				ConsumerWork work = new ConsumerWork(this);
				works.add(work);
				ra.getBootstrapContext().getWorkManager().scheduleWork(work);
			} catch (WorkException e) {
				LOG.error("{}.start()", this, e);
			}
		}
	}

	public void stop() {
		for (ConsumerWork w : works) {
			w.stop();
		}
	}

	private static class ConsumerWork implements Work, ExceptionListener {
		private JMSActivation jmsActivation;
		private boolean stopped = false;
		private XAResourceWrapper xAResourceWrapper;
		private MessageEndpoint messageEndPoint;
		private XAConnection con;
		private MessageConsumer consumer;
		private CountDownLatch latch;

		ConsumerWork(JMSActivation jmsActivation) {
			this.jmsActivation = jmsActivation;
			xAResourceWrapper = new XAResourceWrapper(null);
			try {
				this.messageEndPoint = jmsActivation.endpointFactory.createEndpoint(xAResourceWrapper);
			} catch (UnavailableException e) {
				LOG.error("Error en conexión", e);
			}
		}

		public void setup() throws JMSException {
			synchronized (this) {
				latch = new CountDownLatch(1);
				tryConnect();
			}
		}

		private void tryConnect() {
			int wait = RETRY_MS;
			boolean notConnected = true;
			do {
				if (this.stopped) {
					break;
				}
				try {
					XASession sessTemp = populaConsumer();
					notConnected = false;
					LOG.info("Arrancado el consumidor: {} en session: {}", consumer, sessTemp);
				} catch (JMSException e) {
					LOG.error("Error al arrancar el consumidor", e);
					try {
						Thread.sleep(wait);
						if (wait < 5 * RETRY_MS) {
							wait += wait;
						}
					} catch (InterruptedException ie) {
						// do nothing
					}
				}
			} while (notConnected);
		}

		private XASession populaConsumer() throws JMSException {
			XAConnection conTemp = null;
			MessageConsumer consumerTemp = null;
			conTemp = this.jmsActivation.spec.getXAConnectionFactory().createXAConnection();
			conTemp.setExceptionListener(this);
			XASession sessTemp = conTemp.createXASession();
			Destination destination = sessTemp.createQueue(this.jmsActivation.spec.getQueue());
			consumerTemp = sessTemp.createConsumer(destination);
			XAResource xaRes = sessTemp.getXAResource();
			xAResourceWrapper.setXAResource(xaRes);
			consumerTemp.setMessageListener((MessageListener) this.messageEndPoint);
			con = conTemp;
			consumer = consumerTemp;
			con.start();
			return sessTemp;
		}

		public void stop() {
			synchronized (this) {
				this.stopped = true;
				latch.countDown();
				if (null != consumer) {
					try {
						this.consumer.close();
					} catch (JMSException e) {
						LOG.error("Error en el cerrado de la conexión", e);
					}
				}
				if (null != con) {
					try {
						this.con.close();
					} catch (JMSException e) {
						LOG.error("Error en el cerrado de la conexión", e);
					}
				}
			}
		}

		/**
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			LOG.info("Esperando por un mensaje ...");
			while (!stopped) {
				try {
					setup();
					latch.await();
					stop();
				} catch (InterruptedException e) {
					LOG.error("Se ha interumpido el thread Contenedor", e);
				} catch (JMSException e) {
					LOG.error("Error de JMS en el Thread contenedor", e);
				}
			}
			LOG.info("Terminada tarea Tarea");
		}

		/**
		 * 
		 * @see jakarta.resource.spi.work.Work#release()
		 */
		@Override
		public void release() {
			LOG.trace("{}.release()", this);
			// ¿?
		}

		/**
		 * 
		 * @see jakarta.jms.ExceptionListener#onException(jakarta.jms.JMSException)
		 */
		@Override
		public void onException(JMSException exception) {
			LOG.error("Error en la conexión", exception);
			tryConnect();
		}
	}

	/**
	 * @return the endpointFactory
	 */
	public MessageEndpointFactory getEndpointFactory() {
		return endpointFactory;
	}

}
