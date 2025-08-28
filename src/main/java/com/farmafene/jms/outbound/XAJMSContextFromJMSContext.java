package com.farmafene.jms.outbound;

import java.io.Serializable;

import javax.transaction.xa.XAResource;

import jakarta.jms.BytesMessage;
import jakarta.jms.ConnectionMetaData;
import jakarta.jms.Destination;
import jakarta.jms.ExceptionListener;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.JMSRuntimeException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.StreamMessage;
import jakarta.jms.TemporaryQueue;
import jakarta.jms.TemporaryTopic;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.XAJMSContext;

public class XAJMSContextFromJMSContext implements XAJMSContext {

	private JMSContext ctx;

	public XAJMSContextFromJMSContext(JMSContext ctx) {
		this.ctx = ctx;
	}

	//
	//
	// From XAJMSContext
	//
	public JMSContext getContext() {
		// FIXME - ¿?
		return this.ctx;
	}

	/**
	 * Returns an {@code XAResource} to the caller.
	 *
	 * @return an {@code XAResource}
	 */
	public XAResource getXAResource() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * Returns whether the session is in transacted mode; this method always returns
	 * true.
	 *
	 * @return true
	 */
	@Override
	public boolean getTransacted() {
		// FIXME - ¿?
		return true;
	}

	/**
	 * 
	 * @see jakarta.jms.XAJMSContext#commit()
	 */
	@Override
	public void commit() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.XAJMSContext#rollback()
	 */
	@Override
	public void rollback() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}
	//
	//
	//
	//

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createContext(int)
	 */
	@Override
	public JMSContext createContext(int sessionMode) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createProducer()
	 */
	@Override
	public JMSProducer createProducer() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#getClientID()
	 */
	@Override
	public String getClientID() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#setClientID(java.lang.String)
	 */
	@Override
	public void setClientID(String clientID) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#getMetaData()
	 */
	@Override
	public ConnectionMetaData getMetaData() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 */
	@Override
	public ExceptionListener getExceptionListener() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#setExceptionListener(jakarta.jms.ExceptionListener)
	 */
	@Override
	public void setExceptionListener(ExceptionListener listener) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#start()
	 */
	@Override
	public void start() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#stop()
	 */
	@Override
	public void stop() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#setAutoStart(boolean)
	 */
	@Override
	public void setAutoStart(boolean autoStart) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#getAutoStart()
	 */
	@Override
	public boolean getAutoStart() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#close()
	 */
	@Override
	public void close() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createBytesMessage()
	 */
	@Override
	public BytesMessage createBytesMessage() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createMapMessage()
	 */
	@Override
	public MapMessage createMapMessage() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createMessage()
	 */
	@Override
	public Message createMessage() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createObjectMessage()
	 */
	@Override
	public ObjectMessage createObjectMessage() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createObjectMessage(java.io.Serializable)
	 */
	@Override
	public ObjectMessage createObjectMessage(Serializable object) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createStreamMessage()
	 */
	@Override
	public StreamMessage createStreamMessage() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createTextMessage()
	 */
	@Override
	public TextMessage createTextMessage() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createTextMessage(java.lang.String)
	 */
	@Override
	public TextMessage createTextMessage(String text) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#getSessionMode()
	 */
	@Override
	public int getSessionMode() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#recover()
	 */
	@Override
	public void recover() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createConsumer(jakarta.jms.Destination)
	 */
	@Override
	public JMSConsumer createConsumer(Destination destination) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createConsumer(jakarta.jms.Destination, java.lang.String)
	 */
	@Override
	public JMSConsumer createConsumer(Destination destination, String messageSelector) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createConsumer(jakarta.jms.Destination, java.lang.String, boolean)
	 */
	@Override
	public JMSConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createQueue(java.lang.String)
	 */
	@Override
	public Queue createQueue(String queueName) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createTopic(java.lang.String)
	 */
	@Override
	public Topic createTopic(String topicName) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createDurableConsumer(jakarta.jms.Topic, java.lang.String)
	 */
	@Override
	public JMSConsumer createDurableConsumer(Topic topic, String name) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createDurableConsumer(jakarta.jms.Topic, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public JMSConsumer createDurableConsumer(Topic topic, String name, String messageSelector, boolean noLocal) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createSharedDurableConsumer(jakarta.jms.Topic, java.lang.String)
	 */
	@Override
	public JMSConsumer createSharedDurableConsumer(Topic topic, String name) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createSharedDurableConsumer(jakarta.jms.Topic, java.lang.String, java.lang.String)
	 */
	@Override
	public JMSConsumer createSharedDurableConsumer(Topic topic, String name, String messageSelector) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createSharedConsumer(jakarta.jms.Topic, java.lang.String)
	 */
	@Override
	public JMSConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createSharedConsumer(jakarta.jms.Topic, java.lang.String, java.lang.String)
	 */
	@Override
	public JMSConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName, String messageSelector) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createBrowser(jakarta.jms.Queue)
	 */
	@Override
	public QueueBrowser createBrowser(Queue queue) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}


	/**
	 * 
	 * @see jakarta.jms.JMSContext#createBrowser(jakarta.jms.Queue, java.lang.String)
	 */
	@Override
	public QueueBrowser createBrowser(Queue queue, String messageSelector) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createTemporaryQueue()
	 */
	@Override
	public TemporaryQueue createTemporaryQueue() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#createTemporaryTopic()
	 */
	@Override
	public TemporaryTopic createTemporaryTopic() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#unsubscribe(java.lang.String)
	 */
	@Override
	public void unsubscribe(String name) {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}

	/**
	 * 
	 * @see jakarta.jms.JMSContext#acknowledge()
	 */
	@Override
	public void acknowledge() {
		// FIXME - ¿?
		throw new JMSRuntimeException("Not implemented yet!");
	}
}
