package rit.stu;

import rit.cs.Node;
import rit.cs.Stack;

/**
 * A stack implementation that uses a Node to represent the structure.
 *
 * @param <T> The type of data the stack will hold
 * @author RIT CS
 */
public class StackNode<T> implements Stack<T> {
    private Node<T> top;
    private int size;

    /**
     * Create an empty stack.
     */
    public StackNode() {
        top = null;
        size = 0;
    }

    @Override
    public boolean empty() {
        return size == 0;
    }

    @Override
    public T pop() {
        if (empty()) {
            throw new AssertionError("Stack is empty");
        }

        T element = top.getData();
        top = top.getNext();
        size--;
        return element;
    }

    @Override
    public void push(T element) {
        Node<T> newNode = new Node<>(element);
        newNode.setNext(top);
        top = newNode;
        size++;
    }

    @Override
    public T top() {
        if (empty()) {
            throw new AssertionError("Stack is empty");
        }
        return top.getData();
    }
}