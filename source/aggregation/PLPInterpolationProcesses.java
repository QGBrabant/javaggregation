/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aggregation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import miscellaneous.Couple;
import miscellaneous.TupleImpl;
import orders.BTuple;
import orders.DistributiveLattice;
import orders.impls.BTupleImpl;

/**
 *
 * @author qgbrabant
 */
public class PLPInterpolationProcesses {

    /**
     * Implementation of Algorithm 1 of "Interpolation of partial functions by
     * lattice polynomial functions: a polynomial time algorithm". Returns the
     * constraints specifying the set of lattice polynomial functions that are
     * exactly compatible with each (input,output) pairs in the dataset.
     *
     * @param <T> 
     * @param dataset partial function (may be) from L^n to L
     * @return a Couple whose left part is the set of lower constraints, and
     * right part is the set of upper constraints.
     */
    public static <T extends BTuple> Couple<Map<BTupleImpl, T>>
            computeLPConstraints(AggregationTable<T> dataset) {
        
        DistributiveLattice<T> L = dataset.getSpace();
        int k = L.getDimensionality();
        int n = dataset.getArity();
        Map<BTupleImpl, T> Cp = new HashMap<>();
        Map<BTupleImpl, T> Cm = new HashMap<>();
        Boolean[] G = new Boolean[n];
        TupleImpl<T> input;
        BTuple output;

        for (Entry<AggregationTable<T>.Input, T> e : dataset.pointSet()) {
            input = e.getKey().getInput();
            output = e.getValue();
            for (int d = 0; d < k; d++) {
                for (int i = 0; i < n; i++) {
                    G[i] = input.get(i).get(d);
                }
                if (output.get(d)) {
                    update(L, Cm, new BTupleImpl(G), L.getStPlus(d), true);
                } else {
                    update(L, Cp, new BTupleImpl(G), L.getStMinus(d), false);
                }
            }
        }
        return new Couple(Cm, Cp);
    }

    /**
     * Implementation of Algorithm 2 of "Interpolation of partial functions by
     * lattice polynomial functions: a polynomial time algorithm". Updates the
     * map C that represents the (upper or lower) constraints with mu(G) >= s,
     * if C contains lower constraints, and with mu(G) <= s, if C contains upper
     * constraints. @param <T> type of the element
     *
     * s in L
     *
     * @param <T> type of the elements in L
     * @param L distributive lattice
     * @param C
     * @param G
     * @param s
     * @param lower true if and only if the function updates lower constraints
     */
    public static <T extends BTuple> void update(DistributiveLattice<T> L, Map<BTupleImpl, T> C, BTupleImpl G, T s, boolean lower) {
        T rex = C.get(G);
        if (rex != null) {
            if (lower) {
                C.put(G, (T) L.join(rex, s));
            } else {
                C.put(G, (T) L.meet(rex, s));
            }
        } else {
            C.put(G, (T) s);
        }
    }

    /**
     * Implementation of Algorithm 3 of "Interpolation of partial functions by
     * lattice polynomial functions: a polynomial time algorithm".
     *
     * @param <T>
     * @param couple a Couple whose left part is the set of lower constraints,
     * and right part is the set of upper constraints.
     * @return true if and only if the constraints given in parameter describe a
     * nonempty set of capacities
     */
    public static <T extends BTupleImpl> boolean isConsistent(Couple<Map<BTupleImpl, T>> couple) {
        Integer r;
        for (Entry<BTupleImpl, T> e1 : couple.getLeft().entrySet()) {
            for (Entry<BTupleImpl, T> e2 : couple.getRight().entrySet()) {
                r = e1.getKey().relation(e2.getKey());
                if (r != null && r >= 0) {
                    r = e1.getValue().relation(e2.getValue());
                    if (r == null || r == -1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /*
    public static <T extends BTupleImpl, U extends  DistributiveLattice<T> & ComplementedLattice<T>> GranularCapacity<T>  exhaustiveLearning(int d, U L, AggregationTable<T> dataset){
        //Initialize the capacity
        GranularCapacity<T> lowerBound = new GranularCapacity<>(d,L);
        GranularCapacity<T> upperBound = new GranularCapacity<>(d,L);
        
        Boolean[] bin = new Boolean[d];
        Arrays.fill(bin,false);
        
        T min = L.getTop();
        T max = L.getBottom();
        T complementsConj;
        T complementsDisj;
        boolean stop = false;
        while(!stop){
            for(Entry<AggregationTable<T>.Input,T> row : dataset.pointSet()){
                complementsConj = L.getTop();
                complementsDisj = L.getBottom();
                for(int i = 0 ; i < d ; i ++){
                    //disjonction of complement is complement of conjunction and vice versa
                    if(bin[i]){
                        complementsDisj = L.meet(complementsDisj,row.getKey().getInput().get(i));
                    }else{
                        complementsConj = L.join(complementsConj,row.getKey().getInput().get(i));
                    }
                }
                complementsConj = L.getComplement(complementsConj);
                complementsDisj = L.getComplement(complementsDisj);

                min = L.join(min,L.meet(row.getValue(),complementsConj));
                max = L.meet(max,L.join(row.getValue(),complementsDisj));

                lowerBound.focalNodes.add(new CapacityNode<>(bin,min));
                upperBound.upperConstraints.add(new CapacityNode<>(bin,max));
  
                assert L.relation(min,max) >= 0;
            }
            stop = !binPlusPlus(bin);
        }
        return mu;
    }*/
    /**
     * Increase the number represented by the bit array by 1. True false if the
     * max value is already reached. Useless.
     *
     * @param bin
     * @return
     */
    private static boolean binPlusPlus(Boolean[] bin) {
        boolean success = false;
        for (int i = bin.length - 1; (i >= 0) && !success; i--) {
            if (!bin[i]) {
                bin[i] = true;
                success = true;
                break;
            } else {
                bin[i] = false;
            }
        }
        return success;
    }
}
