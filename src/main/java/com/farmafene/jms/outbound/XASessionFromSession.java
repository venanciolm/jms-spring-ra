package com.farmafene.jms.outbound;

import java.io.Serializable;

import javax.transaction.xa.XAResource;

import jakarta.jms.BytesMessage;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.Session;
import jakarta.jms.StreamMessage;
import jakarta.jms.TemporaryQueue;
import jakarta.jms.TemporaryTopic;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicSubscriber;
import jakarta.jms.XASession;

public class XASessionFromSession implements XASession {

	private Session session;
	private ConnectionFactory connectionFactory;

	public XASessionFromSession(Session session, ConnectionFactory connectionFactory) {
		this.session = session;
		this.connectionFactory = connectionFactory;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("session=").append(session);
		sb.append(", connectionFactory=").append(connectionFactory);
		sb.append("]");
		return sb.toString();
	}

	public Session getPhysicalSession() {
		return this.session;
	}

	/**
	 * 
	 * @see jakarta.jms.XASession#getSession()
	 */
	@Override
	public Session getSession() throws JMSException {
		return this;
	}

	/**
	 * 
	 * @see jakarta.jms.XASession#getXAResource()
	 */
	public XAResource getXAResource() {
		return new XAResourceImpl(this, this.connectionFactory);
	}

	/**
	 * 
	 * @see jakarta.jms.XASession#getTransacted()
	 */
	@Override
	public boolean getTransacted() throws JMSException {
		return session.getTransacted();
	}

	/**
	 * Throws a {@code TransactionInProgressException}, since it should not be
	 * called for an {@code XASession} object.
	 *
	 * @exception TransactionInProgressException if the method is called on an
	 *                                           {@code XASession}.
	 */
	/**
	 * 
	 * @see jakarta.jms.XASession#commit()
	 */
	@Override
	public void commit() throws JMSException {
		session.commit();
	}

	/**
	 * Throws a {@code TransactionInProgressException}, since it should not be
	 * called for an {@code XASession} object.
	 *
	 * @exception TransactionInProgressException if the method is called on an
	 *                                           {@code XASession}.
	 */
	/**
	 * 
	 * @see jakarta.jms.XASession#rollback()
	 */
	@Override
	public void rollback() throws JMSException {
		session.rollback();
	}

	//
	//
	// Parte de Session
	//
	/**
	 * 
	 * @see jakarta.jms.Session#getAcknowledgeMode()
	 */
	@Override
	public int getAcknowledgeMode() throws JMSException {
		return session.getAcknowledgeMode();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#close()
	 */
	@Override
	public void close() throws JMSException {
		session.close();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#recover()
	 */
	@Override
	public void recover() throws JMSException {
		session.recover();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#run()
	 */
	@Override
	public void run() {
		session.run();
	}

	//
	//
	// Parte de generación.
	/**
	 * 
	 * @see jakarta.jms.Session#createBytesMessage()
	 */
	@Override
	public BytesMessage createBytesMessage() throws JMSException {
		return session.createBytesMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createMapMessage()
	 */
	@Override
	public MapMessage createMapMessage() throws JMSException {
		return session.createMapMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createMessage()
	 */
	@Override
	public Message createMessage() throws JMSException {
		return session.createMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createObjectMessage()
	 */
	@Override
	public ObjectMessage createObjectMessage() throws JMSException {
		return session.createObjectMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createObjectMessage(java.io.Serializable)
	 */
	@Override
	public ObjectMessage createObjectMessage(Serializable object) throws JMSException {
		return session.createObjectMessage(object);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createStreamMessage()
	 */
	@Override
	public StreamMessage createStreamMessage() throws JMSException {
		return session.createStreamMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createTextMessage()
	 */
	@Override
	public TextMessage createTextMessage() throws JMSException {
		return session.createTextMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createTextMessage(java.lang.String)
	 */
	@Override
	public TextMessage createTextMessage(String text) throws JMSException {
		return session.createTextMessage(text);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#getMessageListener()
	 */
	@Override
	public MessageListener getMessageListener() throws JMSException {
		return session.getMessageListener();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#setMessageListener(jakarta.jms.MessageListener)
	 */
	@Override
	public void setMessageListener(MessageListener listener) throws JMSException {
		session.setMessageListener(listener);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createProducer(jakarta.jms.Destination)
	 */
	@Override
	public MessageProducer createProducer(Destination destination) throws JMSException {
		return session.createProducer(destination);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createConsumer(jakarta.jms.Destination)
	 */
	@Override
	public MessageConsumer createConsumer(Destination destination) throws JMSException {
		return session.createConsumer(destination);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createConsumer(jakarta.jms.Destination,
	 *      java.lang.String)
	 */
	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
		return session.createConsumer(destination, messageSelector);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createConsumer(jakarta.jms.Destination,
	 *      java.lang.String, boolean)
	 */
	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal)
			throws JMSException {
		return session.createConsumer(destination, messageSelector, noLocal);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createSharedConsumer(jakarta.jms.Topic,
	 *      java.lang.String)
	 */
	@Override
	public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName) throws JMSException {
		return session.createSharedConsumer(topic, sharedSubscriptionName);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createSharedConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName, String messageSelector)
			throws JMSException {
		return session.createSharedConsumer(topic, sharedSubscriptionName, messageSelector);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createQueue(java.lang.String)
	 */
	@Override
	public Queue createQueue(String queueName) throws JMSException {

		return session.createQueue(queueName);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createTopic(java.lang.String)
	 */
	@Override
	public Topic createTopic(String topicName) throws JMSException {
		return session.createTopic(topicName);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createDurableSubscriber(jakarta.jms.Topic,
	 *      java.lang.String)
	 */
	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
		return session.createDurableSubscriber(topic, name);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createDurableSubscriber(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal)
			throws JMSException {
		return session.createDurableSubscriber(topic, name, messageSelector, noLocal);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createDurableConsumer(jakarta.jms.Topic,
	 *      java.lang.String)
	 */
	@Override
	public MessageConsumer createDurableConsumer(Topic topic, String name) throws JMSException {
		return session.createDurableConsumer(topic, name);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createDurableConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public MessageConsumer createDurableConsumer(Topic topic, String name, String messageSelector, boolean noLocal)
			throws JMSException {
		return session.createDurableConsumer(topic, name, messageSelector, noLocal);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createSharedDurableConsumer(jakarta.jms.Topic,
	 *      java.lang.String)
	 */
	@Override
	public MessageConsumer createSharedDurableConsumer(Topic topic, String name) throws JMSException {
		return session.createSharedConsumer(topic, name);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createSharedDurableConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public MessageConsumer createSharedDurableConsumer(Topic topic, String name, String messageSelector)
			throws JMSException {
		return session.createSharedDurableConsumer(topic, name, messageSelector);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createBrowser(jakarta.jms.Queue)
	 */
	@Override
	public QueueBrowser createBrowser(Queue queue) throws JMSException {
		return session.createBrowser(queue);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createBrowser(jakarta.jms.Queue, java.lang.String)
	 */
	@Override
	public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
		return session.createBrowser(queue, messageSelector);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createTemporaryQueue()
	 */
	@Override
	public TemporaryQueue createTemporaryQueue() throws JMSException {
		return session.createTemporaryQueue();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createTemporaryTopic()
	 */
	@Override
	public TemporaryTopic createTemporaryTopic() throws JMSException {
		return session.createTemporaryTopic();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#unsubscribe(java.lang.String)
	 */
	@Override
	public void unsubscribe(String name) throws JMSException {
		session.unsubscribe(name);
	}
}
