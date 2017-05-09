package wdsr.exercise4b.test;

import wdsr.exercise4b.sender.JmsPersistentSender;

public class MainTest {
	public static void main(String[] args) {
		JmsPersistentSender sender = new JmsPersistentSender();
		sender.sendPersistentMessages();
		sender.sendNonPersistendMessages();
		sender.shutDown();
		System.exit(0);
	}
}
