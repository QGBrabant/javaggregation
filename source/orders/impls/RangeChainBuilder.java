/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import orders.Chain;

/**
 *
 * @author qgbrabant
 */
public class RangeChainBuilder implements ChainBuilder {

    private Set<Double> thresholds;
    private boolean ascending;

    public RangeChainBuilder() {
        this.thresholds = new HashSet<>();
        this.ascending = true;
    }

    public RangeChainBuilder(boolean ascending) {
        this();
        this.ascending = ascending;
    }

    public RangeChainBuilder(Collection<Double> thresholds) {
        this();
        this.thresholds.addAll(thresholds);
    }

    public RangeChainBuilder(Double... thresholds) {
        this();
        for (Double d : thresholds) {
            this.thresholds.add(d);
        }
    }

    public RangeChainBuilder(Collection<Double> thresholds, boolean ascending) {
        this();
        this.thresholds.addAll(thresholds);
        this.ascending = ascending;
    }

    @Override
    public boolean add(Double threshold) {
        assert false;
        return false;
    }

    @Override
    public Lexicon buildChain() {

        List<Double> list = new ArrayList<>(this.thresholds);
        if (this.ascending) {
            Collections.sort(list);
        } else {
            Collections.sort(list, (a, b) -> {
                return -a.compareTo(b);
            });
        }

        Chain chain = new ShortChain(list.size()+1);

        return new Lexicon(chain, list, this.ascending);
    }

    public static class Lexicon implements ChainBuilder.Lexicon {

        private final Chain chain;
        private final List<Double> thresholds;
        private final boolean ascending;

        public Lexicon(Chain chain, List<Double> thresholds, boolean ascending) {
            this.chain = chain;
            this.thresholds = thresholds;
            this.ascending = ascending;
        }

        public Rank get(double d) {
            int i = 0;
            boolean found = false;
            while (!found && i < thresholds.size()) {
                if (this.ascending && d < thresholds.get(i)) {
                    found = true;
                } else if (!this.ascending && d > thresholds.get(i)) {
                    found = true;
                } else {
                    i++;
                }
            }
            //System.out.println(d + " in " + this.thresholds + " = " + (i - 1));
            return this.chain.get(i);
        }

        public Chain getChain() {
            return chain;
        }

        public List<Double> getThresholds() {
            return thresholds;
        }

        public boolean isAscending() {
            return ascending;
        }
    }
}
