package org.wfs.test;

import java.util.concurrent.atomic.AtomicBoolean;

public class MyTest {
	
	private volatile AtomicBoolean at = new AtomicBoolean(false);

	public void change() {
		
		final AtomicBoolean at1 = at;
		
		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(at1.get());
		
	}
	
	public void remove() {
		
		try {
			Thread.sleep(1000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		at.set(true);
	}
	
	public static void main(String[] args) {
		MyTest myTest =new MyTest();
		
		Thread t1=new Thread(new Runnable() {
			
			@Override
			public void run() {
				myTest.change();
			}
		});t1.start();
		Thread t2=new Thread(new Runnable() {
			
			@Override
			public void run() {
				myTest.remove();
			}
		});t2.start();
		
	}
}
