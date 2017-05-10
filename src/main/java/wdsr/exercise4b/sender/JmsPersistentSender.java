package wdsr.exercise4b.sender;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wdsr.exercise4b.sender.JmsPersistentSender;

public class JmsPersistentSender {
	private static final Logger log = LoggerFactory.getLogger(JmsPersistentSender.class);
	
	private final int mode = Session.AUTO_ACKNOWLEDGE;
	private final boolean transacted = false;
	private Session session;
	private Connection connection;
	private MessageProducer producer;
	private Queue queue;
	
	private ActiveMQConnectionFactory connectionFactory;
	
	private final String queueName = "RADEKKANIA.QUEUE";
	
	public JmsPersistentSender() {
		this.connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		setUp();
	}
	
	private void setUp() {
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(transacted, mode);
			queue = session.createQueue(queueName);
			producer = session.createProducer(queue);
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
