/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders;

import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import orders.impls.BTupleImpl;
import orders.impls.MaxSet;

/**
 * A class that contains useful functions that make use of the interfaces defined in the package
 * @author qgbrabant
 */
public abstract class OTools {
    /**
     * Returns a list that respect the partial order given as a parameter, which contains every elements of the set.
     * @param <T>
     * @param order
     * @return a list of elements
     */
    public static <T> List<T> totalOrdering(Order<T> order, Set<T> set){
        List<T> list = new ArrayList<>();
        Integer r;
        for(T e : set){
            int i = 0;
            while(i < list.size()){
                r = order.relation(e,list.get(i));
                if(r != null && r > 0){
                    break;
                }
                i++;
            }
            list.add(i,e);
        }
        return list;
    }
    
    /**
     * Returns a list that respect the order of the poset given as a parameter and contains each of its elements.
     * @param <T>
     * @param poset
     * @return 
     */
    public static <T> List<T> totalOrdering(Poset<T> poset){
        return totalOrdering(poset,poset);
    }
    
    public static Integer cartesianProductOfRelations(Integer r1, Integer r2){
        if (r1 == null || r2 == null) return null;
        if (r1 == 0) return r2;
        if (r2 == 0) return r1;
        if (!Objects.equals(r1, r2)) return null;
        return r2;
    }
    
    
    /**
     * Randomly generates an ordered set of join irreducible elements (+ the bottom element).
     * Let the set fo elements be {x1, ..., xk}.
     * For each i,j such that i < j, we set xi < xj with a probability p.
     * Then we take the transitive closure of the relation.
     * @param k number of join irreducibles
     * @param p probability to set xi < xj
     * @return a list of binary tuples that represent join-irreducibles elements
     */
    public static List<BTupleImpl> randomJoinIrreducibleElements(int k, double p){
        List<BTupleImpl> jis = new LinkedList<>();
        
        Boolean[] zero = new Boolean[k];
        for(int i = 0; i < k ; i ++){
            zero[i]=false;
        }
        jis.add(new BTupleImpl(zero));
        
        Boolean[][] bins = new Boolean[k][k];
        int j;
        for(int i = 0; i < k ; i ++){
            j=0;
            while(j < i){
                bins[i][j]=false;
                if(random() <= p){
                    for(int l = 0; l <= j ; l++){
                        bins[i][l] |= bins[j][l];
                    }
                }
                j++;
            }
            bins[i][j]=true;
            j++;
            while(j < k){
                bins[i][j]=false;
                j++;
            }
            jis.add(new BTupleImpl(bins[i]));
        }
        return jis;
    }
    
    
    //Functions below might be incorrect

    public static <T extends PartiallyOrderable> Set<T> simplifiedSet(Set<T> set, boolean lowerBounds){
        Set<T> newSet = new HashSet<>();
        for(T x : set){
            if(!lowerBounds){
                updateLowerBounds(newSet, x);
            }else{
                updateUpperBounds(newSet, x);
            }
        }
        return newSet;
    }

    public static <T extends PartiallyOrderable> boolean updateLowerBounds(Set<T> lbs, T e){
        return updateBounds(lbs,e,true);
    }
    
    public static <T extends PartiallyOrderable> boolean updateUpperBounds(Set<T> ubs, T e){
        return updateBounds(ubs,e,false);
    }
    
    public static <T extends PartiallyOrderable> boolean updateBounds(Set<T> ubs, T e, boolean lower){
        Iterator<T> it = ubs.iterator();
        PartiallyOrderable bt;
        Integer r;
        while(it.hasNext()){
            bt = it.next();
            r = e.relation(bt);
            if(r != null){
                if((lower && r == 1) || (!lower && r == -1)){
                    it.remove();
                }else if ((lower && r <= 0) || (!lower && r >= 0)) {
                    return false;
                }
            }
        }
        ubs.add(e);
        return true;
    }
    
    public static <T> MaxSet greatestDescendant (Poset<T> order, T x){
        MaxSet<T> set = new MaxSet<>(order);
        for(T y : order){
            if(order.relation(x, y) < 0){
                set.add(y);
            }
        }
        return set;
    }
}
