package wdsr.exercise4b.reciver;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JmsPersistentReciver {
	private List<TextMessage> list = new LinkedList<TextMessage>();
	
	private static final Logger log = LoggerFactory.getLogger(JmsPersistentReciver.class);
	
	private ConnectionFactory factory;
	private Connection connection;
	private Session session;
	private Queue queue;
	private MessageConsumer consumer;
	private QueueBrowser browser;
	
	private final int mode = Session.AUTO_ACKNOWLEDGE;
	private final boolean transacted = false;
	private final String queueName = "RADEKKANIA.QUEUE";
	
	

	public JmsPersistentReciver() {
		this.factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		try {
			this.connection = factory.createConnection();
			this.session = connection.createSession(transacted, mode);
			this.queue = session.createQueue(queueName);
			this.consumer = session.createConsumer(queue);
			/*consumer.setMessageListener((message) -> {
				try {
					TextMessage mssg = (TextMessage) message;
					list.add(mssg);
					log.info("Recive message: " + mssg.getText());
				} catch (JMSException e) {
					log.error("fail on method onMessage", e);
				}
			});*/
		} catch (JMSException e) {
			log.info("fail reciver");
		}
	}
	
	
	public void recive() {
		try {
			connection.start();
			TextMessage mssg = (TextMessage) consumer.receive(100);
			while (mssg != null) {
				list.add(mssg);
				log.info("recived " + mssg.getText());
				mssg = (TextMessage) consumer.receive(100);
			}
			log.info("Recvied: + " + list.size() + " messages");
			shutDown();
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