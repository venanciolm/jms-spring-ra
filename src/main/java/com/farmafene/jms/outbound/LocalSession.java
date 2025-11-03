package com.farmafene.jms.outbound;

import java.io.Serializable;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.BytesMessage;
import jakarta.jms.Destination;
import jakarta.jms.IllegalStateException;
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

public class LocalSession implements Session {
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalSession.class);
	private ManagedJMSConnection managedJMSConnection;
	private String id;

	public LocalSession(ManagedJMSConnection managedJMSConnection) {
		this.managedJMSConnection = managedJMSConnection;
		this.id = UUID.randomUUID().toString();
	}

	private Session getMngtSession() {
		return this.managedJMSConnection.getSession();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LocalSession [Id:" + managedJMSConnection.getId() + "::" + this.id + "::" + getMngtSession() + "]";
	}

	/**
	 * 
	 * @see jakarta.jms.Session#commit()
	 */
	@Override
	public void commit() throws JMSException {
		IllegalStateException ies = new IllegalStateException("Not permited! commit()");
		LOGGER.error("commit()", ies);
		throw ies;
	}

	/**
	 * 
	 * @see jakarta.jms.Session#rollback()
	 */
	@Override
	public void rollback() throws JMSException {
		IllegalStateException ies = new IllegalStateException("Not permited! rollback()");
		LOGGER.error("rollback()", ies);
		throw ies;
	}

	/**
	 * 
	 * @see jakarta.jms.Session#close()
	 */
	@Override
	public void close() throws JMSException {
		LOGGER.trace("close():: do nothing");
	}

	@Override
	public void recover() throws JMSException {
		LOGGER.trace("recover()");
		getMngtSession().recover();

	}

	/**
	 * 
	 * @see jakarta.jms.Session#setMessageListener(jakarta.jms.MessageListener)
	 */
	@Override
	public void setMessageListener(MessageListener listener) throws JMSException {
		LOGGER.trace("setMessageListener(MessageListener: {})", listener);
		getMngtSession().setMessageListener(listener);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#run()
	 */
	@Override
	public void run() {
		LOGGER.trace("run()");
		getMngtSession().run();
	}

	// --------------------------------------------------------

	/**
	 * 
	 * @see jakarta.jms.Session#createBytesMessage()
	 */
	@Override
	public BytesMessage createBytesMessage() throws JMSException {
		LOGGER.trace("createBytesMessage()");
		return getMngtSession().createBytesMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createMapMessage()
	 */
	@Override
	public MapMessage createMapMessage() throws JMSException {
		LOGGER.trace("createMapMessage()");
		return getMngtSession().createMapMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createMessage()
	 */
	@Override
	public Message createMessage() throws JMSException {
		LOGGER.trace("createMessage()");
		return getMngtSession().createMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createObjectMessage()
	 */
	@Override
	public ObjectMessage createObjectMessage() throws JMSException {
		LOGGER.trace("createObjectMessage()");
		return getMngtSession().createObjectMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createObjectMessage(java.io.Serializable)
	 */
	@Override
	public ObjectMessage createObjectMessage(Serializable object) throws JMSException {
		LOGGER.trace("createObjectMessage(Serializable: {})", object);
		return getMngtSession().createObjectMessage(object);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createStreamMessage()
	 */
	@Override
	public StreamMessage createStreamMessage() throws JMSException {
		LOGGER.trace("createStreamMessage()");
		return getMngtSession().createStreamMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createTextMessage()
	 */
	@Override
	public TextMessage createTextMessage() throws JMSException {
		LOGGER.trace("createTextMessage()");
		return getMngtSession().createTextMessage();
	}

	@Override
	public TextMessage createTextMessage(String text) throws JMSException {
		LOGGER.trace("createTextMessage(String: {})", text);
		return getMngtSession().createTextMessage(text);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#getTransacted()
	 */
	@Override
	public boolean getTransacted() throws JMSException {
		LOGGER.trace("getTransacted()");
		return getMngtSession().getTransacted();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#getAcknowledgeMode()
	 */
	@Override
	public int getAcknowledgeMode() throws JMSException {
		LOGGER.trace("getAcknowledgeMode()");
		return getMngtSession().getAcknowledgeMode();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#getMessageListener()
	 */
	@Override
	public MessageListener getMessageListener() throws JMSException {
		LOGGER.trace("getMessageListener()");
		return getMngtSession().getMessageListener();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createProducer(jakarta.jms.Destination)
	 */
	@Override
	public MessageProducer createProducer(Destination destination) throws JMSException {
		LOGGER.trace("createProducer(Destination: {})", destination);
		return getMngtSession().createProducer(destination);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createConsumer(jakarta.jms.Destination)
	 */
	@Override
	public MessageConsumer createConsumer(Destination destination) throws JMSException {
		LOGGER.trace("createConsumer(Destination: {})", destination);
		return getMngtSession().createConsumer(destination);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createConsumer(jakarta.jms.Destination,
	 *      java.lang.String)
	 */
	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
		LOGGER.trace("createConsumer(Destination: {}, String: {})", destination, messageSelector);
		return getMngtSession().createConsumer(destination, messageSelector);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createConsumer(jakarta.jms.Destination,
	 *      java.lang.String, boolean)
	 */
	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal)
			throws JMSException {
		LOGGER.trace("createConsumer(Destination: {}, String: {}, boolean: {})", destination, messageSelector, noLocal);
		return getMngtSession().createConsumer(destination, messageSelector, noLocal);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createSharedConsumer(jakarta.jms.Topic,
	 *      java.lang.String)
	 */
	@Override
	public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName) throws JMSException {
		LOGGER.trace("createSharedConsumer(Topic: {}, String: {})", topic, sharedSubscriptionName);
		return getMngtSession().createSharedConsumer(topic, sharedSubscriptionName);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createSharedConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName, String messageSelector)
			throws JMSException {
		LOGGER.trace("createSharedConsumer(Topic: {}, String: {}, String: {})");
		return getMngtSession().createSharedConsumer(topic, sharedSubscriptionName, messageSelector);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createQueue(java.lang.String)
	 */
	@Override
	public Queue createQueue(String queueName) throws JMSException {
		LOGGER.trace("createQueue(String: {})", queueName);
		return getMngtSession().createQueue(queueName);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createTopic(java.lang.String)
	 */
	@Override
	public Topic createTopic(String topicName) throws JMSException {
		LOGGER.trace("createTopic(String:{})", topicName);
		return getMngtSession().createTopic(topicName);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createDurableSubscriber(jakarta.jms.Topic,
	 *      java.lang.String)
	 */
	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
		LOGGER.trace("createDurableSubscriber(Topic: {}, String: {})", topic, name);
		return getMngtSession().createDurableSubscriber(topic, name);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createDurableSubscriber(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal)
			throws JMSException {
		LOGGER.trace("createDurableSubscriber(Topic: {}, String: {}, String: {}, boolean: {})", topic, name,
				messageSelector, noLocal);
		return getMngtSession().createDurableSubscriber(topic, name, messageSelector, noLocal);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createDurableConsumer(jakarta.jms.Topic,
	 *      java.lang.String)
	 */
	@Override
	public MessageConsumer createDurableConsumer(Topic topic, String name) throws JMSException {
		LOGGER.trace("createDurableConsumer(Topic: {}, String: {})", topic, name);
		return getMngtSession().createDurableConsumer(topic, name);
	}

	@Override
	public MessageConsumer createDurableConsumer(Topic topic, String name, String messageSelector, boolean noLocal)
			throws JMSException {
		LOGGER.trace("createDurableConsumer(Topic: {}, String: {}, String: {}, boolean: {})", topic, name,
				messageSelector, noLocal);
		return getMngtSession().createDurableConsumer(topic, name, messageSelector, noLocal);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createSharedDurableConsumer(jakarta.jms.Topic,
	 *      java.lang.String)
	 */
	@Override
	public MessageConsumer createSharedDurableConsumer(Topic topic, String name) throws JMSException {
		LOGGER.trace("createSharedDurableConsumer(Topic: {}, String: {})", topic, name);
		return getMngtSession().createSharedDurableConsumer(topic, name);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createSharedDurableConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public MessageConsumer createSharedDurableConsumer(Topic topic, String name, String messageSelector)
			throws JMSException {
		LOGGER.trace("createSharedDurableConsumer(Topic: {}, String: {}, String: {})", topic, name, messageSelector);
		return getMngtSession().createSharedDurableConsumer(topic, name, messageSelector);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createBrowser(jakarta.jms.Queue)
	 */
	@Override
	public QueueBrowser createBrowser(Queue queue) throws JMSException {
		LOGGER.trace("createBrowser(Queue: {})", queue);
		return getMngtSession().createBrowser(queue);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createBrowser(jakarta.jms.Queue, java.lang.String)
	 */
	@Override
	public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
		LOGGER.trace("createBrowser(Queue: {}, String: {})", queue, messageSelector);
		return getMngtSession().createBrowser(queue, messageSelector);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createTemporaryQueue()
	 */
	@Override
	public TemporaryQueue createTemporaryQueue() throws JMSException {
		LOGGER.trace("createTemporaryQueue()");
		return getMngtSession().createTemporaryQueue();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createTemporaryTopic()
	 */
	@Override
	public TemporaryTopic createTemporaryTopic() throws JMSException {
		LOGGER.trace("createTemporaryTopic()");
		return getMngtSession().createTemporaryTopic();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#unsubscribe(java.lang.String)
	 */
	@Override
	public void unsubscribe(String name) throws JMSException {
		LOGGER.trace("unsubscribe(String: {})", name);
		getMngtSession().unsubscribe(name);
	}
}
