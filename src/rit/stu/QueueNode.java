package rit.stu;

import rit.cs.Node;
import rit.cs.Queue;

/**
 * A queue implementation that uses a Node to represent the structure.
 *
 * @param <T> The type of data the queue will hold
 * @author RIT CS
 */
public class QueueNode<T> implements Queue<T> {
    private Node<T> front;
    private Node<T> back;

    /**
     * Create an empty queue.
     */
    public QueueNode() {
        front = null;
        back = null;
    }

    @Override
    public T back() {
        if (empty()) {
            throw new AssertionError("Queue is empty");
        }
        return back.getData();
    }

    @Override
    public T dequeue() {
        if (empty()) {
            throw new AssertionError("Queue is empty");
        }
        T data = front.getData();
        front = front.getNext();
        if (front == null) {
            back = null;
        }
        return data;
    }

    @Override
    public boolean empty() {
        return front == null;
    }

    @Override
    public void enqueue(T element) {
        Node<T> newNode = new Node<>(element);
        if (empty()) {
            front = newNode;
        } else {
            back.setNext(newNode);
        }
        back = newNode;
    }

    @Override
    public T front() {
        if (empty()) {
            throw new AssertionError("Queue is empty");
        }
        return front.getData();
    }
}
