package com.farmafene.jms.outbound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.CompletionListener;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageProducer;

public class JMSMessageProducerFromMSSessionHandler implements MessageProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSConnectionConsumerFromJMSSessionHandler.class);
	private MessageProducer messageProducer;
	private JMSSessionHandler jMSSessionHandler;

	public JMSMessageProducerFromMSSessionHandler(JMSSessionHandler sHandler, MessageProducer producer) {
		this.jMSSessionHandler = sHandler;
		this.messageProducer = producer;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("jMSSessionHandler=").append(jMSSessionHandler);
		sb.append(", messageProducer=").append(messageProducer);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#setDisableMessageID(boolean)
	 */
	@Override
	public void setDisableMessageID(boolean value) throws JMSException {
		messageProducer.setDisableMessageID(value);
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#getDisableMessageID()
	 */
	@Override
	public boolean getDisableMessageID() throws JMSException {
		return messageProducer.getDisableMessageID();
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#setDisableMessageTimestamp(boolean)
	 */
	@Override
	public void setDisableMessageTimestamp(boolean value) throws JMSException {
		messageProducer.setDisableMessageTimestamp(value);
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#getDisableMessageTimestamp()
	 */
	@Override
	public boolean getDisableMessageTimestamp() throws JMSException {
		return messageProducer.getDisableMessageTimestamp();
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#setDeliveryMode(int)
	 */
	@Override
	public void setDeliveryMode(int deliveryMode) throws JMSException {
		throw new JMSException(
				"setDeliveryMode(deliveryMode: " + deliveryMode + ") no soportado en entorno gestionado!");
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#getDeliveryMode()
	 */
	@Override
	public int getDeliveryMode() throws JMSException {
		return messageProducer.getDeliveryMode();
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#setPriority(int)
	 */
	@Override
	public void setPriority(int defaultPriority) throws JMSException {
		messageProducer.setPriority(defaultPriority);
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#getPriority()
	 */
	@Override
	public int getPriority() throws JMSException {
		return messageProducer.getPriority();
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#setTimeToLive(long)
	 */
	@Override
	public void setTimeToLive(long timeToLive) throws JMSException {
		messageProducer.setTimeToLive(timeToLive);

	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#getTimeToLive()
	 */
	@Override
	public long getTimeToLive() throws JMSException {
		return messageProducer.getTimeToLive();
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#setDeliveryDelay(long)
	 */
	@Override
	public void setDeliveryDelay(long deliveryDelay) throws JMSException {
		messageProducer.setDeliveryDelay(deliveryDelay);
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#getDeliveryDelay()
	 */
	@Override
	public long getDeliveryDelay() throws JMSException {
		return messageProducer.getDeliveryDelay();
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#getDestination()
	 */
	@Override
	public Destination getDestination() throws JMSException {
		return messageProducer.getDestination();
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#close()
	 */
	@Override
	public void close() throws JMSException {
		JMSException e = new JMSException("close(), no soportado en entorno gestionado!");
		LOGGER.error("Error forzado en close()", e);
		throw e;
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#send(jakarta.jms.Message)
	 */
	@Override
	public void send(Message message) throws JMSException {
		messageProducer.send(message);
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#send(jakarta.jms.Message, int, int, long)
	 */
	@Override
	public void send(Message message, int deliveryMode, int priority, long timeToLive) throws JMSException {
		throw new JMSException("send(message: " + message + ", deliveryMode: " + deliveryMode + ", priority: "
				+ priority + ", timeToLive: " + timeToLive + "), no soportado en entorno gestionado!");

	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#send(jakarta.jms.Destination,
	 *      jakarta.jms.Message)
	 */
	@Override
	public void send(Destination destination, Message message) throws JMSException {
		messageProducer.send(destination, message);
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#send(jakarta.jms.Destination,
	 *      jakarta.jms.Message, int, int, long)
	 */
	@Override
	public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive)
			throws JMSException {
		throw new JMSException("send(destination: " + destination + ", message: " + message + ", deliveryMode: "
				+ deliveryMode + ", priority: " + priority + ", timeToLive:" + timeToLive
				+ "), no soportado en entorno gestionado!");
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#send(jakarta.jms.Message,
	 *      jakarta.jms.CompletionListener)
	 */
	@Override
	public void send(Message message, CompletionListener completionListener) throws JMSException {
		messageProducer.send(message, completionListener);
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#send(jakarta.jms.Message, int, int, long,
	 *      jakarta.jms.CompletionListener)
	 */
	@Override
	public void send(Message message, int deliveryMode, int priority, long timeToLive,
			CompletionListener completionListener) throws JMSException {
		throw new JMSException("send(message: " + message + ", deliveryMode: " + deliveryMode + ", priority: "
				+ priority + ", timeToLive: " + timeToLive + ", completionListener" + completionListener
				+ "), no soportado en entorno gestionado!");
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#send(jakarta.jms.Destination,
	 *      jakarta.jms.Message, jakarta.jms.CompletionListener)
	 */
	@Override
	public void send(Destination destination, Message message, CompletionListener completionListener)
			throws JMSException {
		messageProducer.send(destination, message, completionListener);
	}

	/**
	 * 
	 * @see jakarta.jms.MessageProducer#send(jakarta.jms.Destination,
	 *      jakarta.jms.Message, int, int, long, jakarta.jms.CompletionListener)
	 */
	@Override
	public void send(Destination destination, Message message, int deliveryMode, int priority, long timeToLive,
			CompletionListener completionListener) throws JMSException {
		throw new JMSException("send(destination: " + destination + ", message: " + message + ", deliveryMode: "
				+ deliveryMode + ", priority: " + priority + ", timeToLive: " + timeToLive + ", completionListener"
				+ completionListener + "), no soportado en entorno gestionado!");
	}
}
