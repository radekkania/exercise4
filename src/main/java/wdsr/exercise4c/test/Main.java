package wdsr.exercise4c.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wdsr.exercise4c.subscriber.JmsSubscriber;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		JmsSubscriber subscriber = new JmsSubscriber();
		log.info("Subsriber listening for messages: ");
		subscriber.reciveDurable();
	}
}


/*
 * w przypadku wersji durableSubscriber
 * 
 * Je¿eli subsriber jest w³¹czony, nastepnie wlaczamy publishera
 * messages dodawane sa do MessagesEnqueued oraz MessagesDequeued jednoczsenie
 * 
 * Jezeli subsriber jest wylaczony i w tym czasie wlaczymy publishera
 * zgodnie z oczekiwaniami messages dodawane sa do MessageEnqueued.
 * 
 * w przypadku wersji ze zwyklym consumerem
 * 
 * gdy subsrbiber jest wlaczany po uruchomieniu publishera 
 * messages dodawane sa tylko do MessageEnqueued
 * 
 */
