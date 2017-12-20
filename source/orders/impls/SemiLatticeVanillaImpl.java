/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;
import orders.MeetComputable;
import orders.Poset;
import orders.SemiLattice;

/**
 *
 * @author qgbrabant
 * @param <T>
 */
public class SemiLatticeVanillaImpl<T extends MeetComputable> extends HashSet<T> implements Poset<T>, SemiLattice<T>{
    final private T bottom;
    
    public SemiLatticeVanillaImpl(T bot){
        this.bottom = bot;
    }
    
    @Override
    public T meet(T x, T y) {
        return (T) x.meet(y);
    }

    @Override
    public T getBottom() {
        return this.bottom;
    }

    @Override
    public int getDimensionality() {
        throw new UnsupportedOperationException("No dimensionnality know for vanilla semilattice."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer relation(T x, T y) {
        return x.relation(y);
    }

    @Override
    public T randomValue() {
        int item = ThreadLocalRandom.current().nextInt(0, this.size());
        int i = 0;
        for (T obj : this) {
            if (i == item) {
                return obj;
            }
            i++;
        }
        return null;
    }
    
}
