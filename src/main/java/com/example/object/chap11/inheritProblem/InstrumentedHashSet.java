package com.example.object.chap11.inheritProblem;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;

public class InstrumentedHashSet<E> implements Set<E> {
    private int addCount = 0;
    private Set<E> set;

    public InstrumentedHashSet(Set set) {
        this.set = set;
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return this.set.add(e);
    }
    @Override
    public boolean addAll(Collection<? extends E> c) {
        this.addCount += c.size();
        return this.set.addAll(c);
    }

    public int getAddCount() {return this.addCount;}

    @Override
    public Spliterator<E> spliterator() { return this.set.spliterator(); }
    @Override
    public int size() {return this.set.size();}
    @Override
    public boolean isEmpty() {return this.set.isEmpty();}
    @Override
    public boolean contains(Object o) {return this.set.contains(o);}
    @Override
    public Iterator<E> iterator() {return this.set.iterator();}
    @Override
    public Object[] toArray() {return this.set.toArray();}
    @Override
    public <T> T[] toArray(T[] a) {return this.set.toArray(a);}
    @Override
    public boolean remove(Object o) {return this.set.remove(o);}
    @Override
    public boolean containsAll(Collection<?> c) {return this.set.containsAll(c);}
    @Override
    public boolean retainAll(Collection<?> c) {return this.set.retainAll(c);}
    @Override
    public boolean removeAll(Collection<?> c) {return this.set.removeAll(c);}
    @Override
    public void clear() {this.set.clear();}
}
