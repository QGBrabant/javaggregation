/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import static java.lang.Math.max;
import static java.lang.Math.min;
import java.util.Arrays;
import miscellaneous.Indexable;
import orders.BTuple;
import orders.JoinComputable;
import orders.MeetComputable;

/**
 * Tuples that can represents chain elements 
 * @author qgbrabant
 */
public class Rank implements BTuple, Indexable, MeetComputable, JoinComputable {

    private final int size; //size of the tuple
    private final int height; // before val, all values are true, at and after frontier, all values are false
    private int index;
    
    public Rank(int size, int frontier){
        this.size= size;
        this.height = frontier;
        this.index = frontier;
    }
    
    @Override
    public Boolean get(int i) {
        return i <= height;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Boolean[] toArray() {
        Boolean[] array = new Boolean[this.size];
        Arrays.fill(array, 0, this.height - 1, true);
        Arrays.fill(array, height, this.size - 1, false);
        return array;
    }

    @Override
    public Object meet(Object t) {
        if (t instanceof Rank) {
            return new Rank(this.size,min(this.height, ((Rank) t).height));
        } else {
            return ((MeetComputable) t).meet(this);
        }
    }

    @Override
    public Integer relation(Object o) {
        if (o instanceof Rank) {
            int f = ((Rank) o).height;
            if(this.height == f) return 0;
            if(this.height > f) return -1;
            return 1;
        } else {
            assert false;
            Integer r = ((BTuple) o).relation(this);
            if(r != null) r *= -1;
            return r;
        }
    }

    @Override
    public Object join(Object o) {
        if (o instanceof Rank) {
            return new Rank(this.size,max(this.height, ((Rank) o).height));
        } else {
            return ((JoinComputable) o).join(this);
        }
    }

    @Override
    public void setIndex(int i) {
        this.index = i;
    }

    @Override
    public int getIndex() {
        return this.index;
    }
    
    public int toInt(){
        return this.height;
    }
    
    public String toString(){
        return ""+this.toInt();
    }

    @Override
    public int getNumberOfOnes() {
        return this.height;
    }

    @Override
    public int getNumberOfZeros() {
        return this.size - this.height;
    }

}
