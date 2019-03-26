package org.mk.training;


class Greeting {

    public synchronized void greet() {
        System.out.println("Hello, DTrace!");
    }
}

class GreetingThread extends Thread {

    Greeting greeting;

    GreetingThread(Greeting greeting) {
        this.greeting = greeting;
        super.setDaemon(true);
    }

    public void run() {
        while (true) {
            greeting.greet();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}

public class Greeter {

    public static void main(String[] args) {
        Greeting greeting = new Greeting();
        GreetingThread threads[] = new GreetingThread[4];

        for (int i = 0; i < 4; ++i) {
            threads[i] = new GreetingThread(greeting);
            threads[i].start();
        }
        for (int i = 0; i < 4; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException ie) {
            }
        }
    }
}
