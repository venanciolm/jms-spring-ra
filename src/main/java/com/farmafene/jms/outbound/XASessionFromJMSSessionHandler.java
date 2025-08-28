package com.farmafene.jms.outbound;

import java.io.Serializable;

import javax.transaction.xa.XAResource;

import jakarta.jms.BytesMessage;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.JMSRuntimeException;
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

public class XASessionFromJMSSessionHandler implements XASession {

	private JMSSessionHandler sHandler;

	public XASessionFromJMSSessionHandler(JMSSessionHandler jmsSh) {
		this.sHandler = jmsSh;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("JMSSessionHandler=").append(sHandler);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createBytesMessage()
	 */
	@Override
	public BytesMessage createBytesMessage() throws JMSException {
		return this.sHandler.getXASession().createBytesMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createMapMessage()
	 */
	@Override
	public MapMessage createMapMessage() throws JMSException {
		return this.sHandler.getXASession().createMapMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createMessage()
	 */
	@Override
	public Message createMessage() throws JMSException {
		return this.sHandler.getXASession().createMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createObjectMessage()
	 */
	@Override
	public ObjectMessage createObjectMessage() throws JMSException {
		return this.sHandler.getXASession().createObjectMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createObjectMessage(java.io.Serializable)
	 */
	@Override
	public ObjectMessage createObjectMessage(Serializable object) throws JMSException {
		return this.sHandler.getXASession().createObjectMessage(object);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createStreamMessage()
	 */
	@Override
	public StreamMessage createStreamMessage() throws JMSException {
		return this.sHandler.getXASession().createStreamMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createTextMessage()
	 */
	@Override
	public TextMessage createTextMessage() throws JMSException {
		return this.sHandler.getXASession().createTextMessage();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createTextMessage(java.lang.String)
	 */
	@Override
	public TextMessage createTextMessage(String text) throws JMSException {
		return this.sHandler.getXASession().createTextMessage(text);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#getAcknowledgeMode()
	 */
	@Override
	public int getAcknowledgeMode() throws JMSException {
		return this.sHandler.getXASession().getAcknowledgeMode();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#close()
	 */
	@Override
	public void close() throws JMSException {
		throw new JMSException("close() no soportado en entorno gestionado!");
	}

	/**
	 * 
	 * @see jakarta.jms.Session#run()
	 */
	@Override
	public void run() {
		throw new JMSRuntimeException("run() no soportado en entorno gestionado!");
	}

	/**
	 * 
	 * @see jakarta.jms.Session#recover()
	 */
	@Override
	public void recover() throws JMSException {
		throw new JMSException("recover() no soportado en entorno gestionado!");
	}

	/**
	 * 
	 * @see jakarta.jms.XASession#commit()
	 */
	@Override
	public void commit() throws JMSException {
		throw new JMSException("commit() no soportado en entorno gestionado!");
	}

	/**
	 * 
	 * @see jakarta.jms.XASession#rollback()
	 */
	@Override
	public void rollback() throws JMSException {
		throw new JMSException("rollback() no soportado en entorno gestionado!");
	}

	/**
	 * 
	 * @see jakarta.jms.Session#getMessageListener()
	 */
	@Override
	public MessageListener getMessageListener() throws JMSException {
		return this.sHandler.getXASession().getMessageListener();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#setMessageListener(jakarta.jms.MessageListener)
	 */
	@Override
	public void setMessageListener(MessageListener listener) throws JMSException {
		this.sHandler.getXASession().setMessageListener(listener);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createProducer(jakarta.jms.Destination)
	 */
	@Override
	public MessageProducer createProducer(Destination destination) throws JMSException {
		return new JMSMessageProducerFromMSSessionHandler(this.sHandler,
				this.sHandler.getXASession().createProducer(destination));
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createConsumer(jakarta.jms.Destination)
	 */
	@Override
	public MessageConsumer createConsumer(Destination destination) throws JMSException {
		return new JMSMessageConsumerFromMSSessionHandler(this.sHandler,
				this.sHandler.getXASession().createConsumer(destination));
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createConsumer(jakarta.jms.Destination,
	 *      java.lang.String)
	 */
	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector) throws JMSException {
		return new JMSMessageConsumerFromMSSessionHandler(this.sHandler,
				this.sHandler.getXASession().createConsumer(destination, messageSelector));
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createConsumer(jakarta.jms.Destination,
	 *      java.lang.String, boolean)
	 */
	@Override
	public MessageConsumer createConsumer(Destination destination, String messageSelector, boolean noLocal)
			throws JMSException {
		return new JMSMessageConsumerFromMSSessionHandler(this.sHandler,
				this.sHandler.getXASession().createConsumer(destination, messageSelector, noLocal));
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createSharedConsumer(jakarta.jms.Topic,
	 *      java.lang.String)
	 */
	@Override
	public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName) throws JMSException {
		return new JMSMessageConsumerFromMSSessionHandler(this.sHandler,
				this.sHandler.getXASession().createSharedConsumer(topic, sharedSubscriptionName));
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createSharedConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public MessageConsumer createSharedConsumer(Topic topic, String sharedSubscriptionName, String messageSelector)
			throws JMSException {
		return new JMSMessageConsumerFromMSSessionHandler(this.sHandler,
				this.sHandler.getXASession().createSharedConsumer(topic, sharedSubscriptionName, messageSelector));
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createQueue(java.lang.String)
	 */
	@Override
	public Queue createQueue(String queueName) throws JMSException {
		return this.sHandler.getXASession().createQueue(queueName);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createTopic(java.lang.String)
	 */
	@Override
	public Topic createTopic(String topicName) throws JMSException {
		return this.sHandler.getXASession().createTopic(topicName);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createDurableSubscriber(jakarta.jms.Topic,
	 *      java.lang.String)
	 */
	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name) throws JMSException {
		return new JMSTopicSubscriberFromMSSessionHandler(this.sHandler,
				this.sHandler.getXASession().createDurableSubscriber(topic, name));
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createDurableSubscriber(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String name, String messageSelector, boolean noLocal)
			throws JMSException {
		return new JMSTopicSubscriberFromMSSessionHandler(this.sHandler,
				this.sHandler.getXASession().createDurableSubscriber(topic, name, messageSelector, noLocal));
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createDurableConsumer(jakarta.jms.Topic,
	 *      java.lang.String)
	 */
	@Override
	public MessageConsumer createDurableConsumer(Topic topic, String name) throws JMSException {
		return new JMSMessageConsumerFromMSSessionHandler(this.sHandler,
				this.sHandler.getXASession().createDurableConsumer(topic, name));
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createDurableConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public MessageConsumer createDurableConsumer(Topic topic, String name, String messageSelector, boolean noLocal)
			throws JMSException {
		return new JMSMessageConsumerFromMSSessionHandler(this.sHandler,
				this.sHandler.getXASession().createDurableConsumer(topic, name, messageSelector, noLocal));
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createSharedDurableConsumer(jakarta.jms.Topic,
	 *      java.lang.String)
	 */
	@Override
	public MessageConsumer createSharedDurableConsumer(Topic topic, String name) throws JMSException {
		return new JMSMessageConsumerFromMSSessionHandler(this.sHandler,
				this.sHandler.getXASession().createSharedDurableConsumer(topic, name));
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createSharedDurableConsumer(jakarta.jms.Topic,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public MessageConsumer createSharedDurableConsumer(Topic topic, String name, String messageSelector)
			throws JMSException {
		return new JMSMessageConsumerFromMSSessionHandler(this.sHandler,
				this.sHandler.getXASession().createSharedDurableConsumer(topic, name, messageSelector));
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createBrowser(jakarta.jms.Queue)
	 */
	@Override
	public QueueBrowser createBrowser(Queue queue) throws JMSException {
		return this.sHandler.getXASession().createBrowser(queue);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createBrowser(jakarta.jms.Queue, java.lang.String)
	 */
	@Override
	public QueueBrowser createBrowser(Queue queue, String messageSelector) throws JMSException {
		return this.sHandler.getXASession().createBrowser(queue, messageSelector);
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createTemporaryQueue()
	 */
	@Override
	public TemporaryQueue createTemporaryQueue() throws JMSException {
		return this.sHandler.getXASession().createTemporaryQueue();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#createTemporaryTopic()
	 */
	@Override
	public TemporaryTopic createTemporaryTopic() throws JMSException {
		return this.sHandler.getXASession().createTemporaryTopic();
	}

	/**
	 * 
	 * @see jakarta.jms.Session#unsubscribe(java.lang.String)
	 */
	@Override
	public void unsubscribe(String name) throws JMSException {
		this.sHandler.getXASession().unsubscribe(name);
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
	@Override
	public XAResource getXAResource() {
		return this.sHandler.getXASession().getXAResource();
	}

	/**
	 * 
	 * @see jakarta.jms.XASession#getTransacted()
	 */
	@Override
	public boolean getTransacted() throws JMSException {
		return this.sHandler.getXASession().getTransacted();
	}
}
