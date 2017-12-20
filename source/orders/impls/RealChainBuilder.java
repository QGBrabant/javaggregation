/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import orders.Chain;

/**
 * This class is for building Chains by using double values.
 * Step 1: create a new Chain builder
 Step 2: add all double values that you want in your chain
 Step 3: call buildChain, which provide a RealChainBuilder.Lexicon.
 
 RealChainBuilder.Lexicon contains a Chain and a Map<Double,Rank>,
 * and allows to get the element of the Chain that corresponds to a double (provided this double was added during step 2).
 * @author qgbrabant
 */
public class RealChainBuilder implements ChainBuilder{

    private Set<Double> values;
    private boolean ascending;

    public RealChainBuilder() {
        this.values = new HashSet<>();
        this.ascending = true;
    }

    public RealChainBuilder(boolean ascending) {
        this();
        this.ascending = ascending;
    }

    public boolean add(Double value) {
        return this.values.add(value);
    }

    public Lexicon buildChain() {

        List<Double> list = new ArrayList<>(this.values);
        if (this.ascending) {
            Collections.sort(list);
        } else {
            Collections.sort(list, (a, b) -> {
                return -a.compareTo(b);
            });
        }

        Chain chain = new ShortChain(list.size());
        
        Map<Double,Rank> map  = new HashMap<>();
        
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i), chain.get(i));
        }

        return new Lexicon(chain,map);
    }

    public static class Lexicon implements ChainBuilder.Lexicon{

        private final Chain chain;
        private final Map<Double, Rank> map;

        public Lexicon(Chain chain, Map<Double, Rank> map) {
            this.chain = chain;
            this.map = map;
        }
        
        public Rank get(double d) {
            return this.map.get(d);
        }

        public Chain getChain() {
            return chain;
        }

        public Map<Double, Rank> getMap() {
            return map;
        }
    }
}
