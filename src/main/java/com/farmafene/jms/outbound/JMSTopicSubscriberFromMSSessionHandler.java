package com.farmafene.jms.outbound;

import jakarta.jms.JMSException;
import jakarta.jms.Topic;
import jakarta.jms.TopicSubscriber;

public class JMSTopicSubscriberFromMSSessionHandler extends JMSMessageConsumerFromMSSessionHandler
		implements TopicSubscriber {

	private TopicSubscriber topicSubscriber;

	public JMSTopicSubscriberFromMSSessionHandler(JMSSessionHandler sHandler, TopicSubscriber topicSubscriber) {
		super(sHandler, topicSubscriber);
		this.topicSubscriber = topicSubscriber;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("jMSSessionHandler=").append(getJMSSessionHandler());
		sb.append(", topicSubscriber=").append(topicSubscriber);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 
	 * @see jakarta.jms.TopicSubscriber#getTopic()
	 */
	@Override
	public Topic getTopic() throws JMSException {
		return topicSubscriber.getTopic();
	}

	/**
	 * 
	 * @see jakarta.jms.TopicSubscriber#getNoLocal()
	 */
	@Override
	public boolean getNoLocal() throws JMSException {
		return topicSubscriber.getNoLocal();
	}
}
