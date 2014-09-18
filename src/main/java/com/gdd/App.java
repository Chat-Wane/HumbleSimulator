package com.gdd;

import com.gdd.messaging.Loop;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");

		Loop l = new Loop();
		l.before();
		l.execute();
		l.after();

	}
}
