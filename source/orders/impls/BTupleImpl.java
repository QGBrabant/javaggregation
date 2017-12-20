/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import static java.lang.Math.random;
import java.util.Iterator;
import java.util.Set;
import miscellaneous.TupleImpl;
import orders.BTuple;

/**
 *
 * @author qgbrabant
 */
public class BTupleImpl extends TupleImpl<Boolean> implements BTuple{
    public BTupleImpl(Boolean[] bin){
        super(bin);
    }
    
    public Boolean[] getBin(){
        Boolean[] t = new Boolean[this.size()];
        for(int i = 0; i < this.size(); i++){
            t[i] = this.get(i);
        }
        return t;
    }
    
    public Integer lexicographicRelation(BTupleImpl x){
        for(int i = 0; i < this.size() ; i ++){
            if(this.get(i) && !x.get(i)){
                return -1;
            }else if (!this.get(i) && x.get(i)){
                return 1;
            }
        }
        return 0;
    }
    
    public static BTupleImpl randomBTuple(int d){
        Boolean[] bins = new Boolean[d];
        for(int i = 0; i < d ; i++){
            bins[i] = random() < 0.5 ;
        }
        return new BTupleImpl(bins);
    }
    
    @Override
    public Object join(Object o) {
        BTupleImpl x = (BTupleImpl) o;
        assert x.size() == this.size();

        Boolean[] bin = new Boolean[x.size()];
        for (int i = 0; i < bin.length; i++) {
            bin[i] = x.get(i) || this.get(i);
        }
        return new BTupleImpl(bin);
    }
    
    @Override
    public Object meet(Object o) {
        BTupleImpl x = (BTupleImpl) o;
        assert x.size() == this.size();
        
        Boolean[] bin = new Boolean[x.size()];
        for (int i = 0; i < bin.length; i++) {
            bin[i] = x.get(i) && this.get(i);
        }
        return new BTupleImpl(bin);
    }
    
    public static <T extends BTupleImpl> boolean updateLowerBounds(Set<T> lbs, T e){
        Iterator<T> it = lbs.iterator();
        BTupleImpl bt;
        Integer r;
        while(it.hasNext()){
            bt = it.next();
            r = e.relation(bt);
            if(r != null){
                if(r == 1){
                    it.remove();
                }else if ( r <= 0 ) {
                    return false;
                }
            }
        }
        lbs.add(e);
        return true;
    }
    
    public static <T extends BTupleImpl> boolean updateUpperBounds(Set<T> ubs, T e){
        Iterator<T> it = ubs.iterator();
        BTupleImpl bt;
        Integer r;
        while(it.hasNext()){
            bt = (BTupleImpl) it.next();
            r = e.relation(bt);
            if(r != null){
                if(r == -1){
                    it.remove();
                }else if ( r >= 0 ) {
                    return false;
                }
            }
        }
        ubs.add(e);
        return true;
    }

    @Override
    public Integer relation(Object o) {
        if(!(o instanceof BTupleImpl)){
            assert false;
            return null;
        }
        BTupleImpl x = (BTupleImpl) o;
        Integer rel = 0;
        for(int i = 0; i < this.size() ; i ++){
            if(this.get(i) && !x.get(i)){
                if(rel == 1){
                    return null;
                }else{
                    rel = -1;
                }
            }else if (!this.get(i) && x.get(i)){
                if(rel == -1){
                    return null;
                }else{
                    rel = 1;
                }
            }
        }
        return rel;
    }

    @Override
    public int getNumberOfOnes() {
        int res = 0;
        for(Boolean b : this.array){
            if(b) res ++;
        }
        return res;
    }

    @Override
    public int getNumberOfZeros() {
        return this.array.length - this.getNumberOfOnes();
    }
}
