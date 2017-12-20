/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import orders.Chain;

/**
 *
 * @author qgbrabant
 */


public class ShortChain extends HashSet<Rank> implements Chain{
    private Rank[] elements;
    
    public ShortChain(int size){
        this.elements = new Rank[size];
        for(int i = 0 ; i < size ; i ++){
            this.elements[i] = new Rank(size,i);
            this.add(this.elements[i]);
        }
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
        return this.elements[max(x.toInt(),y.toInt())];
    }

    @Override
    public Rank getTop() {
        return this.elements[this.elements.length-1];
    }
    
    public Rank get(int i){
        return this.elements[i];
    }

    @Override
    public Rank randomValueBetween(Rank min, Rank max) {
        //System.out.println(min.getFrontier() +" "+ max.getFrontier());
        return this.elements[ThreadLocalRandom.current().nextInt(min.toInt(), max.toInt()+1)];
    }

    @Override
    public Rank meet(Rank x, Rank y) {
        return this.elements[min(x.toInt(),y.toInt())];
    }

    @Override
    public Rank getBottom() {
        return this.elements[0];
    }

    @Override
    public int getDimensionality() {
        return this.elements.length;
    }

    @Override
    public Integer relation(Rank x, Rank y) {
        return x.relation(y);
    }

    @Override
    public Rank randomValue() {
         return this.elements[ThreadLocalRandom.current().nextInt(0, this.elements.length)];
    }
    
}
