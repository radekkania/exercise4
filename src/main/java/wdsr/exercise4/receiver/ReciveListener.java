package wdsr.exercise4.receiver;

import java.math.BigDecimal;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wdsr.exercise4.PriceAlert;
import wdsr.exercise4.VolumeAlert;

public class ReciveListener implements MessageListener {
	
	private static final Logger log = LoggerFactory.getLogger(ReciveListener.class);

	private AlertService service;
	
	public ReciveListener(AlertService service) {
		this.service = service;
	}
	/*
	 * Implementation of method from MessageListener.
	 * (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message message) {
		
		if (message instanceof ObjectMessage) {
			log.info("object message");
			ObjectMessage object = (ObjectMessage) message;
			processObjectMessage(object);
		}
		
		if (message instanceof TextMessage) {
			log.info("text message");
			TextMessage text = (TextMessage) message;
			processTextMessage(text);
		}
	}
	
	/**
	 * Method process Message when it is an instance of ObjectMessage
	 * @param mssg
	 */
	private void processObjectMessage(ObjectMessage mssg)  {
		try {
			if (mssg.getJMSType().equals("PriceAlert")) {
				PriceAlert alert = (PriceAlert) mssg.getObject();
				service.processPriceAlert(alert);
			}
			if (mssg.getJMSType().equals("VolumeAlert")) {
				VolumeAlert alert = (VolumeAlert) mssg.getObject();
				service.processVolumeAlert(alert);
			}
		} catch (JMSException e) {
			log.error("failed", e);
		}
	}
	
	/**
	 * Method process Message when it is an instanceof TextMessage
	 * @param mssg
	 */
	private void processTextMessage(TextMessage mssg) {
		try {
			String message = mssg.getText();
			String[] elements = getElementsFromText(message);
			if (mssg.getJMSType().equals("PriceAlert")) {
				PriceAlert alert = new PriceAlert(Long.valueOf(elements[0]), 
						String.valueOf(elements[1]), new BigDecimal(elements[2]));
				service.processPriceAlert(alert);
			}
			if (mssg.getJMSType().equals("VolumeAlert")) {
				VolumeAlert alert = new VolumeAlert(Long.valueOf(elements[0]), 
						String.valueOf(elements[1]), Long.valueOf(elements[2]));
				service.processVolumeAlert(alert);
			}
		} catch (JMSException e) {
			log.error("failed", e);
		}
	}

	//moze przerobic na mape ?
	private String[] getElementsFromText(String text) {
		String[] processedText = text.split("\n");
		String[] output = new String[3];
		int i = 0;
		for (String s : processedText) {
			output[i] = s.substring(s.indexOf("=")+1, s.length()).trim();
			++i;
		}
		return output;
	}

}
