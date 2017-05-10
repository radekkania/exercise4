package wdsr.exercise4b.test;

import wdsr.exercise4c.publisher.JmsPublisher;

public class MainTest {
	public static void main(String[] args) {
		JmsPublisher publisher = new JmsPublisher();
		publisher.sendNonPersistendMessages();
		publisher.sendPersistentMessages();
		publisher.shutDown();
	}
}
