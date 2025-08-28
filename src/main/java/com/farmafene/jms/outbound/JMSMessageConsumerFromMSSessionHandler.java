package com.farmafene.jms.outbound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;

public class JMSMessageConsumerFromMSSessionHandler implements MessageConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSConnectionConsumerFromJMSSessionHandler.class);
	private MessageConsumer messageConsumer;
	private JMSSessionHandler jMSSessionHandler;

	public JMSMessageConsumerFromMSSessionHandler(JMSSessionHandler sHandler, MessageConsumer consumer) {
		this.jMSSessionHandler = sHandler;
		this.messageConsumer = consumer;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("jMSSessionHandler=").append(jMSSessionHandler);
		sb.append(", messageConsumer=").append(messageConsumer);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Devuelve el manejador de la session
	 * 
	 * @return actual JMSSessionHandler
	 */
	protected JMSSessionHandler getJMSSessionHandler() {
		return jMSSessionHandler;
	}

	/**
	 * 
	 * @see jakarta.jms.MessageConsumer#getMessageSelector()
	 */
	@Override
	public String getMessageSelector() throws JMSException {
		return messageConsumer.getMessageSelector();
	}

	/**
	 * 
	 * @see jakarta.jms.MessageConsumer#getMessageListener()
	 */
	@Override
	public MessageListener getMessageListener() throws JMSException {
		return messageConsumer.getMessageListener();
	}

	/**
	 * 
	 * @see jakarta.jms.MessageConsumer#setMessageListener(jakarta.jms.MessageListener)
	 */
	@Override
	public void setMessageListener(MessageListener listener) throws JMSException {
		// messageConsumer.setMessageListener(listener);
		JMSException e = new JMSException(
				"setMessageListener(listener: " + listener + "), no es poportado en un entorno gestionado");
		LOGGER.error("setMessageListener(listener: {})", listener, e);
		throw e;
	}

	/**
	 * 
	 */
	@Override
	public Message receive() throws JMSException {
		return messageConsumer.receive();
	}

	/**
	 * 
	 * @see jakarta.jms.MessageConsumer#receive(long)
	 */
	@Override
	public Message receive(long timeout) throws JMSException {
		return messageConsumer.receive(timeout);
	}

	/**
	 * 
	 * @see jakarta.jms.MessageConsumer#receiveNoWait()
	 */
	@Override
	public Message receiveNoWait() throws JMSException {
		return messageConsumer.receiveNoWait();
	}

	/**
	 * 
	 * @see jakarta.jms.MessageConsumer#close()
	 */
	@Override
	public void close() throws JMSException {
		JMSException e = new JMSException("close(), no es poportado en un entorno gestionado");
		LOGGER.error("close()", e);
		throw e;
	}
}
