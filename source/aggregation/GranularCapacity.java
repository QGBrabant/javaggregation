/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aggregation;

import static java.lang.Math.ceil;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import miscellaneous.TupleImpl;
import orders.DistributiveLattice;
import orders.OTools;
import orders.impls.BTupleImpl;

/**
 *
 * @author qgbrabant
 * @param <T>
 */

public class GranularCapacity<T extends BTupleImpl> implements Capacity<T> {
    private final int arity;
    private final DistributiveLattice<T> space;
    private final Set<CapacityNode<T>> focalNodes;

    public GranularCapacity(int d, DistributiveLattice<T> space) {
        this.arity = d;
        this.space = space;
        this.focalNodes = new HashSet<>();
    }

    public GranularCapacity(int d, DistributiveLattice<T> space, Set<CapacityNode<T>> nodes) {
        this.arity = d;
        this.space = space;
        this.focalNodes = OTools.simplifiedSet(nodes, false);
    }

    public static GranularCapacity randomGranularCapacity(int d, DistributiveLattice<? extends BTupleImpl> space, double smoothness) {
        GranularCapacity<? extends BTupleImpl> mu = new GranularCapacity(d, space);
        mu.randomize(smoothness);
        return mu;
    }

    public void randomize(double smoothness) {
        int n = (int) ceil(pow(2, ((double) this.getArity()) + log(smoothness)));

        Set<BTupleImpl> tupleSet = new HashSet<>();
        Boolean[] bin = new Boolean[this.getArity()];
        while (tupleSet.size() < n) {
            for (int i = 0; i < this.getArity(); i++) {
                bin[i] = (random() < 0.5);
            }
            tupleSet.add(new BTupleImpl(bin));
        }

        List<BTupleImpl> tupleList = new ArrayList<>(tupleSet);
        List<CapacityNode<T>> constraints = new ArrayList<>();
        T min ;
        T max ;
        CapacityNode<T> cc;
        Integer r;
        for (int i = 0; i < tupleList.size(); i++) {
            min = this.getCodomain().getBottom();
            max = this.getCodomain().getTop();
            for (int j = 0; j < i; j++) {
                r = tupleList.get(i).relation(tupleList.get(j));
                if (r != null && r == -1) {
                    min = this.getCodomain().join(min, constraints.get(j).getConstraint());
                }
                r = tupleList.get(i).relation(tupleList.get(j));
                if (r != null && r == 1) {
                    max = this.getCodomain().meet(max, constraints.get(j).getConstraint());
                }
            }
            cc = new CapacityNode(tupleList.get(i).getBin(), this.getCodomain().randomValueBetween(min, max));
            constraints.add(cc);
            this.focalNodes.add(cc);
        }
    }

    @Override
    public T getSugenoOutput(TupleImpl<T> input) {
        int d = this.getCodomain().getDimensionality();
        T result = this.getCodomain().getBottom();
        T threshold;
        for (CapacityNode<T> cc : this.focalNodes) {
            threshold = cc.getContent();
            for (int i = 0; i < input.size(); i++) {
                if (cc.get(i)) {
                    threshold = this.getCodomain().meet(threshold, input.get(i));
                }
            }
            result = this.getCodomain().join(result, threshold);
        }
        return result;
    }
    
    public Set<CapacityNode<T>> getFocalNodes() {
        return new HashSet<>(focalNodes);
    }

    @Override
    public int getArity() {
        return this.arity;
    }

    @Override
    public DistributiveLattice<T> getCodomain() {
        return this.space;
    }

}
