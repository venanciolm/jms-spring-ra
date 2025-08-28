package com.farmafene.jms.outbound;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.commons.tx.XAErrorsException;
import com.farmafene.commons.tx.XAHelper;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.XASession;

public class XAResourceImpl implements XAResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(XAResourceImpl.class);
	private XASession xASession;
	private ConnectionFactory connectionFactory;
	private int transactionTimeout;

	public XAResourceImpl(XASession xASession, ConnectionFactory connectionFactory) {
		this.xASession = xASession;
		this.connectionFactory = connectionFactory;
	}

	/**
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("[");
		sb.append("xASession=").append(xASession.getClass().getCanonicalName());
		sb.append(", ").append(xASession);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * 
	 * @see javax.transaction.xa.XAResource#isSameRM(javax.transaction.xa.XAResource)
	 */
	@Override
	public boolean isSameRM(XAResource xares) throws XAException {
		boolean isSameRM = false;
		if (XAResourceImpl.class.isAssignableFrom(xares.getClass())) {
			isSameRM = ((XAResourceImpl) xares).connectionFactory == this.connectionFactory;
		}
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("isSameRM(XAResource: {}):={}, wrapped: {}", xares, isSameRM,
					xASession.getClass().getCanonicalName());
		}
		return isSameRM;
	}

	/**
	 * 
	 * @see javax.transaction.xa.XAResource#recover(int)
	 */
	@Override
	public Xid[] recover(int flag) throws XAException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("revover(flags: {}), wrapped: {}", XAHelper.getStringFromFlag(flag),
					xASession.getClass().getCanonicalName());
		}
		return new Xid[0];
	}

	/**
	 * 
	 * @see javax.transaction.xa.XAResource#start(javax.transaction.xa.Xid, int)
	 */
	@Override
	public void start(Xid xid, int flags) throws XAException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("start(flags: {}, Xid: {}), wrapped: {}", XAHelper.getStringFromFlag(flags), xid,
					xASession.getClass().getCanonicalName());
		}
	}

	@Override
	public void end(Xid xid, int flags) throws XAException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("end(flags: {}, Xid: {}), wrapped: {}", XAHelper.getStringFromFlag(flags), xid,
					xASession.getClass().getCanonicalName());
		}
	}

	/**
	 * 
	 * @see javax.transaction.xa.XAResource#prepare(javax.transaction.xa.Xid)
	 */
	@Override
	public int prepare(Xid xid) throws XAException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("prepare(Xid: {}), wrapped: {}", xid, xASession.getClass().getCanonicalName());
		}
		return XAResource.XA_OK;
	}

	/**
	 * 
	 * @see javax.transaction.xa.XAResource#commit(javax.transaction.xa.Xid,
	 *      boolean)
	 */
	@Override
	public void commit(Xid xid, boolean onePhase) throws XAException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("commit(onePhase: {}, Xid: {}), wrapped: {}", onePhase, xid,
					xASession.getClass().getCanonicalName());
		}
		try {
			xASession.commit();
		} catch (JMSException e) {
			final XAErrorsException ex = new XAErrorsException("Error en el commit", XAException.XAER_RMERR, e);
			throw ex;
		}
	}

	/**
	 * 
	 * @see javax.transaction.xa.XAResource#rollback(javax.transaction.xa.Xid)
	 */
	@Override
	public void rollback(Xid xid) throws XAException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("rollback(Xid: {}), wrapped: {}", xid, xASession.getClass().getCanonicalName());
		}
		try {
			xASession.rollback();
		} catch (JMSException e) {
			final XAErrorsException ex = new XAErrorsException("Error en el rollback", XAException.XAER_RMERR, e);
			throw ex;
		}
	}

	/**
	 * 
	 * @see javax.transaction.xa.XAResource#forget(javax.transaction.xa.Xid)
	 */
	@Override
	public void forget(Xid xid) throws XAException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("forget(Xid: {}), wrapped: {}", xid, xASession.getClass().getCanonicalName());
		}
	}

	/**
	 * 
	 * @see javax.transaction.xa.XAResource#getTransactionTimeout()
	 */
	@Override
	public int getTransactionTimeout() throws XAException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("getTransactionTimeout():={}, wrapped: {}", transactionTimeout,
					xASession.getClass().getCanonicalName());
		}
		return transactionTimeout;
	}

	/**
	 * 
	 * @see javax.transaction.xa.XAResource#setTransactionTimeout(int)
	 */
	@Override
	public boolean setTransactionTimeout(int seconds) throws XAException {
		boolean out = true;
		transactionTimeout = seconds;
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("setTransactionTimeout(seconds: {}):={}, wrapped: {}", seconds, out,
					xASession.getClass().getCanonicalName());
		}
		return out;
	}
}
