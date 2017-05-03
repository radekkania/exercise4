package wdsr.exercise4.sender;

import java.math.BigDecimal;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wdsr.exercise4.Order;

public class JmsSender {
	private static final Logger log = LoggerFactory.getLogger(JmsSender.class);

	private final int mode = Session.AUTO_ACKNOWLEDGE;
	private final boolean transacted = false;
	private Session session;
	private Connection connection;
	
	private ActiveMQConnectionFactory connectionFactory;
	
	private final String queueName;
	private final String topicName;
	
	public JmsSender(final String queueName, final String topicName) {
		this.queueName = queueName;
		this.topicName = topicName;
		this.connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:62616");
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(transacted, mode);
		} catch (Exception e) {
			log.error("creating connection and session failed", e);
		}
	}

	/**
	 * This method creates an Order message with the given parameters and sends it as an ObjectMessage to the queue.
	 * @param orderId ID of the product
	 * @param product Name of the product
	 * @param price Price of the product
	 */
	public void sendOrderToQueue(final int orderId, final String product, final BigDecimal price) {

		try {
			Destination destination = session.createQueue(queueName);
			MessageProducer producer = session.createProducer(destination);
			
			//create Order object and message
			Order order = new Order(orderId, product, price);
			ObjectMessage mssg = session.createObjectMessage();
			mssg.setObject(order);
			mssg.setJMSType("Order");
			mssg.setStringProperty("WDSR-System", "OrderProcessor");

			//send the message
			producer.send(mssg);
			session.close();
			connection.close();
			log.info("success");
		} catch (JMSException e) {
			log.info("failed",e.toString());
		}
	}

	/**
	 * This method sends the given String to the queue as a TextMessage.
	 * @param text String to be sent
	 */
	public void sendTextToQueue(String text) {
		try {
			Destination destination = session.createQueue(queueName);
			MessageProducer producer = session.createProducer(destination);
			
			//create Order object and message
			TextMessage mssg = session.createTextMessage();
			mssg.setText(text);
			mssg.setJMSType("Order");
			mssg.setStringProperty("WDSR-System", "OrderProcessor");

			//send the message
			producer.send(mssg);
			session.close();
			connection.close();
			log.info("Method SendTextToQueue complited successfully");
		} catch (JMSException e) {
			log.info("Method SendTextToQueue failed", e.toString());
		}
	}

	/**
	 * Sends key-value pairs from the given map to the topic as a MapMessage.
	 * @param map Map of key-value pairs to be sent.
	 */
	public void sendMapToTopic(Map<String, String> map) {
		try {
			Destination destination = session.createTopic(topicName);
			MessageProducer producer = session.createProducer(destination);
			
			//create Order object and message
			MapMessage mssg = session.createMapMessage();
			
			for (Map.Entry<String, String> entryMap : map.entrySet()) {
				mssg.setString(entryMap.getKey(), entryMap.getValue());
			}
		
			//send the message
			producer.send(mssg);
			session.close();
			connection.close();
			log.info("success");
		} catch (JMSException e) {
			log.info("failed",e.toString());
		}
	}
}
