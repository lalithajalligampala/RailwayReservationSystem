package com.railway.adsa;

import java.util.LinkedList;
import java.util.Queue;

public class WaitingQueue {

    private Queue<Integer> queue;

    public WaitingQueue() {
        queue = new LinkedList<Integer>();
    }

    // Add passenger to waiting list
    public void enqueue(int passengerId) {
        queue.add(passengerId);
    }

    // Remove first passenger from waiting list
    public int dequeue() {

        if (queue.isEmpty()) {
            return -1;
        }

        return queue.remove();
    }

    // View first passenger
    public int peek() {

        if (queue.isEmpty()) {
            return -1;
        }

        return queue.peek();
    }

    // Check whether queue is empty
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    // Get number of passengers waiting
    public int size() {
        return queue.size();
    }
}