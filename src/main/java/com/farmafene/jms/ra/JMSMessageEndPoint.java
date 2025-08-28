package com.farmafene.jms.ra;

import java.lang.reflect.Method;

import javax.transaction.xa.XAResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.farmafene.commons.tx.XAHelper;

import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.endpoint.MessageEndpoint;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.Status;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;

public class JMSMessageEndPoint implements MessageEndpoint, MessageListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSMessageEndPoint.class);
	private XAResource xAResource;
	private MessageListener endpoint;
	private TransactionManager transactionManager;

	public JMSMessageEndPoint(MessageListener endpoint, TransactionManager transactionManager, XAResource xaResource) {
		this.endpoint = endpoint;
		this.transactionManager = transactionManager;
		this.xAResource = xaResource;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("transactionManager=").append(transactionManager);
		sb.append(", xAResource=").append(xAResource);
		sb.append(", endpoint=").append(endpoint);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 
	 * @see jakarta.resource.spi.endpoint.MessageEndpoint#beforeDelivery(java.lang.reflect.Method)
	 */
	@Override
	public void beforeDelivery(Method method) throws NoSuchMethodException, ResourceException {
		LOGGER.trace("{}.beforeDelivery(method: {})", this, method);
		if (null != transactionManager) {
			try {
				if (this.transactionManager.getTransaction() == null) {
					LOGGER.trace("beforeDelivery(Method: {}).begin()", method);
					this.transactionManager.begin();
				}
			} catch (NotSupportedException e) {
				LOGGER.error("beforeDelivery(KO, Method: {}).begin()", method, e);
				throw new ResourceException(e);
			} catch (SystemException e) {
				LOGGER.error("beforeDelivery(KO, Method: {}).begin()", method, e);
				throw new ResourceException(e);
			} catch (IllegalStateException e) {
				LOGGER.error("beforeDelivery(KO, Method: {}).begin()", method, e);
				throw new ResourceException(e);
			}
			try {
				if (this.transactionManager.getTransaction() != null) {
					if (null != xAResource) {
						LOGGER.trace("enlistResource({})", xAResource);
						this.transactionManager.getTransaction().enlistResource(xAResource);
					} else {
						LOGGER.warn("No disponemos de un XAResource que enlistart");
					}
				}
			} catch (RollbackException e) {
				LOGGER.error("beforeDelivery(KO, Method: {}).enlistResource(..)", method, e);
				throw new ResourceException(e);
			} catch (IllegalStateException e) {
				LOGGER.error("beforeDelivery(KO, Method: {}).enlistResource(..)", method, e);
				throw new ResourceException(e);
			} catch (SystemException e) {
				LOGGER.error("beforeDelivery(KO, Method: {}).enlistResource(..)", method, e);
				throw new ResourceException(e);
			}
			LOGGER.trace("beforeDelivery(OK, Method: {})", method);
		}
	}

	/**
	 * 
	 * @see jakarta.jms.MessageListener#onMessage(jakarta.jms.Message)
	 */
	@Override
	public void onMessage(Message message) {
		LOGGER.trace("{}.onMessage(Message: {})", this, message);
		try {
			beforeDelivery(null);
			if (null != transactionManager) {
				try {
					endpoint.onMessage(message);
					LOGGER.trace("onMessage(OK, Message: {})", message);
				} catch (Throwable th) {
					LOGGER.error("onMessage(KO, Message: {})", message, th);
					try {
						this.transactionManager.setRollbackOnly();
					} catch (IllegalStateException e) {
						LOGGER.error("onMessage(KO, Message: {})", message, th);
					} catch (SystemException e) {
						LOGGER.error("onMessage(KO, Message: {})", message, th);
					}
				}
			} else {
				try {
					endpoint.onMessage(message);
				} catch (Throwable th) {
					LOGGER.error("onMessage(KO, Message: {})", message, th);
				}
			}
		} catch (NoSuchMethodException e) {
			LOGGER.error("onMessage(KO, Message: {})", message, e);
		} catch (ResourceException e) {
			LOGGER.error("onMessage(KO, Message: {})", message, e);
		}
		try {
			afterDelivery();
		} catch (ResourceException e) {
			LOGGER.error("onMessage().afterDelivery(KO, Message: {})", message, e);
		}
	}

	/**
	 * 
	 * @see jakarta.resource.spi.endpoint.MessageEndpoint#afterDelivery()
	 */
	@Override
	public void afterDelivery() throws ResourceException {
		LOGGER.trace("{}.afterDelivery()", this);
		if (null != transactionManager) {
			try {
				if (transactionManager.getTransaction() != null) {
					LOGGER.trace("afterDelivery({})",
							XAHelper.getStringFromStatus(transactionManager.getTransaction()));
					switch (transactionManager.getTransaction().getStatus()) {
					case Status.STATUS_ACTIVE:
						LOGGER.trace("afterDelivery(){}", ".commit()");
						transactionManager.commit();
						break;
					case Status.STATUS_MARKED_ROLLBACK:
						LOGGER.trace("afterDelivery(){}", ".rollback()");
						transactionManager.rollback();
						break;
					case Status.STATUS_PREPARED:
					case Status.STATUS_COMMITTED:
					case Status.STATUS_ROLLEDBACK:
					case Status.STATUS_NO_TRANSACTION:
					case Status.STATUS_PREPARING:
					case Status.STATUS_COMMITTING:
					case Status.STATUS_ROLLING_BACK:
					case Status.STATUS_UNKNOWN:
					default:
						// do nothing o mandar exepción
						break;
					}
				} else {
					LOGGER.trace("afterDelivery({})", "<???>");
				}
			} catch (SystemException e) {
				LOGGER.error("afterDelivery()", e);
				throw new ResourceException(e);
			} catch (IllegalStateException e) {
				LOGGER.error("afterDelivery()", e);
				throw new ResourceException(e);
			} catch (SecurityException e) {
				LOGGER.error("afterDelivery()", e);
				throw new ResourceException(e);
			} catch (RollbackException e) {
				LOGGER.error("afterDelivery()", e);
				throw new ResourceException(e);
			} catch (HeuristicMixedException e) {
				LOGGER.error("afterDelivery()", e);
				throw new ResourceException(e);
			} catch (HeuristicRollbackException e) {
				LOGGER.error("afterDelivery()", e);
				throw new ResourceException(e);
			}
		}
	}

	/**
	 * 
	 * @see jakarta.resource.spi.endpoint.MessageEndpoint#release()
	 */
	@Override
	public void release() {
		LOGGER.trace("{}.release()", this);
	}
}
