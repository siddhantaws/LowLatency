package com.wfs.locking.lockingpatterns;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Siddhanta Kumar Pattnaik
 */
public class ConcurrentLinkedDequeTest {

    public static void main(String[] args) {
        ConcurrentLinkedDeque<String> strings =new ConcurrentLinkedDeque<String>();
        strings.add("A");
        strings.add("B");strings.add("C");strings.add("D");
    }
}
