package wdsr.exercise4c.publisher;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wdsr.exercise4c.publisher.JmsPublisher;

public class JmsPublisher {
	private static final Logger log = LoggerFactory.getLogger(JmsPublisher.class);
	
	private final int mode = Session.AUTO_ACKNOWLEDGE;
	private final boolean transacted = false;
	private Session session;
	private Connection connection;
	private MessageProducer producer;
	private Topic topic;
	
	private ActiveMQConnectionFactory connectionFactory;
	
	private final String topicName = "RADEKKANIA.TOPIC";
	
	public JmsPublisher() {
		this.connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		setUp();
	}
	
	private void setUp() {
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(transacted, mode);
			topic = session.createTopic(topicName);
			producer = session.createProducer(topic);
		} catch (Exception e) {
			log.error("creating connection and session failed", e);
		}
	}
	
	public void sendPersistentMessages() {
		sendMessage(DeliveryMode.PERSISTENT, "persistent");
	}
	
	public void sendNonPersistendMessages() {
		sendMessage(DeliveryMode.NON_PERSISTENT, "non_persistent");
	}
	
	private void sendMessage(int mode, String stringMode) {
		try {
			producer.setDeliveryMode(mode);
			long startTime = System.currentTimeMillis();
			for (int i=0; i<10000; ++i) {
				TextMessage mssg = session.createTextMessage("test_" + i+1);
				mssg.setJMSDeliveryMode(mode);
				producer.send(mssg);
			}
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			String info = String.format("10000 %s messages sent in %d milliseconds ", stringMode, elapsedTime);
			log.info(info);
		} catch (JMSException e) {
			log.error("sending persistent messages failed", e);
		} 
	}
	
	public void shutDown() {
		try {
			connection.close();
			session.close();
		} catch (JMSException e) {
			log.error("shutting down failed", e);
		}
	}
}
