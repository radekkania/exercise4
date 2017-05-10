package wdsr.exercise4c.subscriber;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JmsSubscriber {	
	private static final Logger log = LoggerFactory.getLogger(JmsSubscriber.class);
	
	private ConnectionFactory factory;
	private Connection connection;
	private Session session;
	private Topic topic;
	private MessageConsumer consumer;
	private TopicSubscriber subscriber;
	
	private final int mode = Session.AUTO_ACKNOWLEDGE;
	private final boolean transacted = false;
	private final String topicName = "RADEKKANIA.TOPIC";

	public JmsSubscriber() {
		this.factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		try {
			this.connection = factory.createConnection();
			this.connection.setClientID("myClientId");
			this.session = connection.createSession(transacted, mode);
			this.topic = session.createTopic(topicName);
			subscriber = session.createDurableSubscriber(topic, "testSub");
			subscriber.setMessageListener((message) -> {
				try {
					TextMessage mssg = (TextMessage) message;
					log.info("Recive message: " + mssg.getText());
				} catch (JMSException e) {
					log.error("fail on method onMessage", e);
				}
			});
		} catch (JMSException e) {
			log.error("failed", e);
		} 
	}
	
	public void closeDurableSubscriber() {
		if (subscriber != null) {
			try {
				subscriber.close();
			} catch (JMSException e) {
				log.error("error occured while closing subsriber");
			}
		}
	}
	
	public void recive() {
		try {
			connection.start();
		} catch (JMSException e) {
			log.error("failed");
		} 
	}
		
	private void shutDown() {
		try {
			consumer.setMessageListener(null);
			session.close();
			connection.close();
		} catch (JMSException e) {
			log.error("cant close session or connection", e);
		}
	}
}