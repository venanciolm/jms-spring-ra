package com.farmafene.jms.ra;

import java.util.UUID;
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
import jakarta.resource.spi.work.Work;

class JMSInBoundXAResource implements XAResource, Work, ExceptionListener {
	private static final Logger LOG = LoggerFactory.getLogger(JMSInBoundXAResource.class);
	private static final int RETRY_MS = 60000;

	private String id;
	private boolean stopped = false;
	private JMSActivation jmsActivation;
	private MessageEndpoint messageEndPoint;
	private XAConnection con;
	private XASession sess;
	private MessageConsumer consumer;
	private CountDownLatch latch;
	private XAResource internal;

	JMSInBoundXAResource(JMSActivation jmsActivation) {
		this.jmsActivation = jmsActivation;
		this.id = UUID.randomUUID().toString();
		try {
			this.messageEndPoint = this.jmsActivation.endpointFactory.createEndpoint(this);
		} catch (UnavailableException e) {
			throw new UnsupportedOperationException(e);
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
		sb.append(", stopped=").append(stopped);
		sb.append(", queue=").append(jmsActivation.spec.getQueue());
		sb.append(", consumer=").append(consumer);
		sb.append(", sess=").append(sess);
		sb.append(", con=").append(con);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Inicializa el contenedor generando las conexiones
	 * 
	 * @throws JMSException
	 */
	public void setup() throws JMSException {
		synchronized (this) {
			latch = new CountDownLatch(1);
			physicalConnect();
		}
	}

	/**
	 * Realiza la parada del contenedor.
	 */
	public void stop() {
		synchronized (this) {
			this.stopped = true;
			latch.countDown();
		}
	}

	/**
	 * Realiza la conexión a JMS en modo listener
	 */
	public void physicalConnect() {
		int wait = RETRY_MS;
		boolean notConnected = true;
		do {
			if (this.stopped) {
				break;
			}
			try {
				this.con = this.jmsActivation.spec.getXAConnectionFactory().createXAConnection();
				this.con.setExceptionListener(this);
				this.sess = this.con.createXASession();
				Destination destination = sess.createQueue(this.jmsActivation.spec.getQueue());
				this.consumer = sess.createConsumer(destination);
				this.consumer.setMessageListener((MessageListener) this.messageEndPoint);
				con.start();
				this.internal = sess.getXAResource();
				notConnected = false;
				LOG.info("Arrancado el consumidor: {}", this);
			} catch (JMSException e) {
				LOG.error("Error al arrancar el consumidor {}", this, e);
				physicalClose();
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

	/**
	 * Cierra las conexiones, sesiones, consumidores físicos.
	 */
	public void physicalClose() {
		LOG.info("Parando el consumidor: {}", this);
		if (null != consumer) {
			try {
				this.consumer.close();
			} catch (JMSException e) {
				LOG.error("Error en el cerrado de la el consumer", e);
			} finally {
				this.consumer = null;
			}
		}
		if (null != this.sess) {
			try {
				this.sess.close();
			} catch (JMSException e) {
				LOG.error("Error en el cerrado de la session", e);
			} finally {
				this.internal = null;
				this.sess = null;
			}
		}
		if (null != this.con) {
			try {
				this.con.close();
			} catch (JMSException e) {
				LOG.error("Error en el cerrado de la conexión", e);
			} finally {
				this.con = null;
			}
		}
	}

	/**
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (!stopped) {
			try {
				setup();
				LOG.info("Esperando por un mensaje ...");
				latch.await();
				physicalClose();
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
		physicalClose();
		physicalConnect();
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