package org.example;

import java.util.Iterator;
import java.util.function.Consumer;

public interface CustomIterator<E> extends Iterator<E> {
    boolean hasNext();
    E next();
    void forEachRemaining(Consumer<? super E> action);
}
