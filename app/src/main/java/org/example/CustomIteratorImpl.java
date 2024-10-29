package org.example;

public class CustomIteratorImpl<T> implements CustomIterator<T> {
    private Node<T> currentNode;

    public CustomIteratorImpl(Node<T> startNode) {
        this.currentNode = startNode;
    }

    @Override
    public boolean hasNext() {
        return currentNode != null;
    }

    @Override
    public T next() {
        T data = currentNode.data;
        currentNode = currentNode.next;
        return data;
    }

    @Override
    public void forEachRemaining(java.util.function.Consumer<? super T> action) {
        while (hasNext()) {
            action.accept(next());
        }
    }
}
