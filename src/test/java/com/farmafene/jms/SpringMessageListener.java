package com.farmafene.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;

import com.farmafene.commons.tx.XAHelper;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import jakarta.transaction.Transactional;

public class SpringMessageListener implements MessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(SpringMessageListener.class);

	/**
	 * 
	 * @see jakarta.jms.MessageListener#onMessage(jakarta.jms.Message)
	 */
	@Override
	@Transactional(rollbackOn = Throwable.class)
	public void onMessage(Message message) {
		if (TextMessage.class.isAssignableFrom(message.getClass())) {
			TextMessage msg = (TextMessage) message;
			try {
				StringBuilder sb = new StringBuilder();
				String lf = System.lineSeparator();
				sb.append(lf).append("/+---------------------------------------+");
				sb.append(lf).append(" | Consumer:");
				sb.append(lf).append(" +---------------------------------------+");
				sb.append(lf).append(" | Msg:   ").append(msg.getText());
				sb.append(lf).append(" +---------------------------------------+");
				LOG.info("{}", sb);
			} catch (JMSException e) {
				throw new UnsupportedOperationException("Mensaje invalido", e);
			} catch (Exception e) {
				throw new UnsupportedOperationException("Mensaje invalido", e);
			}
			// throw new UnsupportedOperationException("Mensaje invalido");
		} else {
			throw new UnsupportedOperationException("Mensaje invalido");
		}
		org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization(//
				new TransactionSynchronization() {

					/**
					 * @see org.springframework.transaction.support.TransactionSynchronization#beforeCommit(boolean)
					 */
					@Override
					public void beforeCommit(boolean readOnly) {
						dolog("beforeCommit", readOnly ? "RO" : "RW");
					}

					/**
					 * @see org.springframework.transaction.support.TransactionSynchronization#beforeCompletion()
					 */
					@Override
					public void beforeCompletion() {
						dolog("beforeCompletion", null);
					}

					/**
					 * @see org.springframework.transaction.support.TransactionSynchronization#afterCommit()
					 */
					@Override
					public void afterCommit() {
						dolog("afterCommit", null);
					}

					/**
					 * @see org.springframework.transaction.support.TransactionSynchronization#afterCompletion(int)
					 */
					@Override
					public void afterCompletion(int status) {
						dolog("afterCompletion", XAHelper.getStringFromStatus(status));
					}

					private void dolog(String method, String value) {
						StringBuilder sb = new StringBuilder();
						String lf = System.lineSeparator();
						sb.append(lf).append("/+---------------------------------------+");
						sb.append(lf).append(" | ").append(SpringMessageListener.this);
						sb.append(lf).append(" | .").append(method).append("(").append(value == null ? "" : value)
								.append(")");
						sb.append(lf).append(" +---------------------------------------+");
						LOG.info("{}", sb);
					}

				} //

		);
	}
}
