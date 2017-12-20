/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import orders.Chain;

/**
 *
 * @author qgbrabant
 */
public class LongChain implements Chain {

    private Integer upperBound;
    private Integer lowerBound;
    private Map<Integer, Rank> elements;

    public LongChain(int size) {
        this.upperBound = size - 1;
        this.lowerBound = 0;
        this.elements = new HashMap<>();
    }

    @Override
    public Rank getStPlus(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Rank getStMinus(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Rank> getJointIrreducibles() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Rank join(Rank x, Rank y) {
        if(x.toInt() < y.toInt()){
            return this.get(y.toInt());
        }else{
            return this.get(x.toInt());
        }
    }

    @Override
    public Rank getTop() {
        return this.get(this.upperBound);
    }

    @Override
    public Rank get(int i) {
        if (i < this.lowerBound || i > this.upperBound) {
            assert false;
            return null;
        }
        Rank res = this.elements.get(i);
        if (res == null) {
            res = new Rank(this.upperBound + 1, i);
            this.elements.put(i, res);
        }
        return res;
    }

    @Override
    public Rank randomValueBetween(Rank min, Rank max) {
        return this.get(ThreadLocalRandom.current().nextInt(min.toInt(), max.toInt() + 1));
    }

    @Override
    public Rank meet(Rank x, Rank y) {
        if(x.toInt() > y.toInt()){
            return this.get(y.toInt());
        }else{
            return this.get(x.toInt());
        }
    }

    @Override
    public Rank getBottom() {
        return this.get(this.lowerBound);
    }

    @Override
    public int getDimensionality() {
        return this.upperBound - this.lowerBound + 1;
    }

    @Override
    public Integer relation(Rank x, Rank y) {
        return x.relation(y);
    }

    @Override
    public Rank randomValue() {
        return this.get(ThreadLocalRandom.current().nextInt(this.lowerBound, this.upperBound));
    }

    @Override
    public int size() {
        return this.upperBound - this.lowerBound;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        Rank r = (Rank) o;
        return this.elements.values().contains(r);
        
    }

    @Override
    public Iterator<Rank> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T[] toArray(T[] arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean add(Rank e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addAll(Collection<? extends Rank> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
