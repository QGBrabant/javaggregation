/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aggregation;

import static java.lang.Math.ceil;
import static java.lang.Math.log;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import miscellaneous.TupleImpl;
import orders.DistributiveLattice;
import orders.OTools;
import orders.impls.BTupleImpl;

/**
 * Implementation of the capacity where the nodes are linked wrt their ordering,
 * like in a Hass diagram.
 *
 * @author qgbrabant
 * @param <T>
 */
public class LinkedCapacity<T extends BTupleImpl> implements Capacity<T> {

    final private ConstraintNode lowerRoot;
    final private ConstraintNode upperRoot;
    private int arity;
    private DistributiveLattice<T> space;

    private class ConstraintNode extends BTupleImpl {

        public Set<Link> upperLinks;
        public Set<Link> lowerLinks;
        public Object lastSeen;
        public T value;

        public ConstraintNode(Boolean[] bin) {
            super(bin);
            this.upperLinks = new HashSet<>();
            this.lowerLinks = new HashSet<>();
            this.lastSeen = null;
        }

        public ConstraintNode(Boolean[] bin, T c) {
            super(bin);
            this.upperLinks = new HashSet<>();
            this.lowerLinks = new HashSet<>();
            this.value = c;
            this.lastSeen = null;
        }

        public List<Integer> computeDiffs(ConstraintNode cn) {
            List<Integer> l = new ArrayList<>();
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i) != cn.get(i)) {
                    l.add(i);
                }
            }
            return l;
        }

        private void add(ConstraintNode cn, boolean up) {
            if (this.lastSeen == cn) {
                return;
            }
            this.lastSeen = cn;

            Integer r;
            boolean transmit = false;
            boolean delete = false;

            Iterator<Link> iterator;
            if (up) {
                iterator = this.upperLinks.iterator();
            } else {
                iterator = this.lowerLinks.iterator();
            }

            while (iterator.hasNext()) {
                Link l = iterator.next();
                r = l.target.relation(cn);
                if (r != null) {
                    if (r == 0) {
                        assert false;
                    } else if ((up && r > 0) || ((!up) && r < 0)) {
                        transmit = true;
                        assert !delete;
                        l.target.add(cn, up);
                    } else {
                        assert !transmit;
                        iterator.remove();
                        delete = true;
                    }
                }
            }
            if (!transmit) {
                List<Integer> diffs = cn.computeDiffs(this);
                if (up) {
                    this.upperLinks.add(new Link(cn, diffs));
                    cn.lowerLinks.add(new Link(this, diffs));
                } else {
                    this.lowerLinks.add(new Link(cn, diffs));
                    cn.upperLinks.add(new Link(this, diffs));
                }

            }

        }

        public void setRandomValue(DistributiveLattice<T> L) {
            T min = L.getBottom();
            T max = L.getTop();
            for (Link l : upperLinks) {
                max = L.meet(max, l.target.value);
            }
            for (Link l : lowerLinks) {
                min = L.join(min, l.target.value);
            }
            Integer r = L.relation(min, max);
            assert r != null && r >= 0;
            this.value = L.randomValueBetween(min, max);
        }

        @Override
        public boolean equals(Object o) {
            ConstraintNode cn = (ConstraintNode) o;
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i) != cn.get(i)) {
                    return false;
                }
            }
            return true;
        }
    }

    private class Link {

        List<Integer> diff;
        ConstraintNode target;

        public Link(ConstraintNode target, List<Integer> diff) {
            this.diff = diff;
            this.target = target;
        }

        @Override
        public int hashCode() {
            return target.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            Link l = (Link) o;
            return this.target.equals(l.target) && this.diff.equals(l.diff);
        }

        @Override
        public String toString() {
            return "->" + target;
        }
    }

    public LinkedCapacity(int arity, DistributiveLattice<T> space) {
        this.arity = arity;
        this.space = space;
        Boolean[] bin = new Boolean[arity];
        Arrays.fill(bin, false);
        this.lowerRoot = new ConstraintNode(bin, space.getBottom());
        Arrays.fill(bin, true);
        this.upperRoot = new ConstraintNode(bin, space.getBottom());
        List<Integer> diffs = this.upperRoot.computeDiffs(this.lowerRoot);
        this.lowerRoot.upperLinks.add(new Link(this.upperRoot, diffs));
        this.upperRoot.lowerLinks.add(new Link(this.lowerRoot, diffs));
    }

    public LinkedCapacity(int arity, DistributiveLattice<T> space, Set<CapacityNode<T>> lowerConstraints, Set<CapacityNode<T>> upperConstraints) {
        this.arity = arity;
        this.space = space;

        lowerConstraints = OTools.simplifiedSet(lowerConstraints, false);

        Boolean[] bin = new Boolean[arity];
        Arrays.fill(bin, false);
        this.lowerRoot = new ConstraintNode(bin, space.getBottom());
        Arrays.fill(bin, true);
        this.upperRoot = new ConstraintNode(bin, space.getBottom());
        List<Integer> diffs = this.upperRoot.computeDiffs(this.lowerRoot);
        this.lowerRoot.upperLinks.add(new Link(this.upperRoot, diffs));
        this.upperRoot.lowerLinks.add(new Link(this.lowerRoot, diffs));

        ConstraintNode cn;
        for (CapacityNode<T> c : lowerConstraints) {
            cn = new ConstraintNode(c.getBin(), c.getConstraint());
            if (cn.equals(this.lowerRoot)) {
                this.lowerRoot.value = c.getConstraint();
            } else if (cn.equals(this.upperRoot)) {
                this.upperRoot.value = c.getConstraint();
            } else {
                this.addNode(cn);
            }

        }
    }

    public static <T extends BTupleImpl> LinkedCapacity randomInstance(int arity, DistributiveLattice<? extends T> space, double smoothness) {
        int granules = (int) ceil(pow(2, ((double) arity) + log(smoothness)));
        return randomInstance(arity, space, granules);
    }

    /**
     * Randomly generates and returns an instance of LinkedCapacity
     * @param <T> type of values returned by the capacity
     * @param arity
     * @param space codomain of the capacity
     * @param granules maximal numer of focal sets
     * @return 
     */
    public static <T extends BTupleImpl> LinkedCapacity randomInstance(int arity, DistributiveLattice<? extends T> space, int granules) {
        LinkedCapacity res = new LinkedCapacity(arity, space);

        granules = min(granules, (int) pow(2, arity));

        Set<BTupleImpl> tupleSet = new HashSet<>();
        Boolean[] bin = new Boolean[arity];
        while (tupleSet.size() < granules) {
            for (int i = 0; i < res.getArity(); i++) {
                bin[i] = (random() < 0.5);
            }
            tupleSet.add(new BTupleImpl(bin));
        }

        LinkedCapacity.ConstraintNode toAdd;
        for (BTupleImpl bt : tupleSet) {
            toAdd = res.new ConstraintNode(bt.getBin());
            if (toAdd.equals(res.lowerRoot)) {
                res.lowerRoot.setRandomValue(space);
            } else if (toAdd.equals(res.upperRoot)) {
                res.upperRoot.setRandomValue(space);
            } else {
                res.addNode(toAdd);
                toAdd.setRandomValue(space);
            }

        }

        return res;
    }

    private void addNode(ConstraintNode cn) {
        this.lowerRoot.add(cn, true);
        this.upperRoot.add(cn, false);
    }

    @Override
    public T getSugenoOutput(TupleImpl<T> input) {
        ConstraintNode current = this.lowerRoot;
        Boolean[] bin = new Boolean[0];
        ConstraintNode result = new ConstraintNode(bin, this.getCodomain().getBottom());

        recMinOutput(result, current, this.getCodomain().getTop(), input);

        return result.value;
    }

    //Result is a variable for border effects on the computed result, allowing coordination between the branches of recursion.
    private void recMinOutput(ConstraintNode result, ConstraintNode currentNode, T currentValue, TupleImpl<T> input) {
        currentNode.lastSeen = result;

        DistributiveLattice<T> L = this.getCodomain();

        Integer r = L.relation(currentValue, result.value);
        if (r != null && r >= 0) {
            return;
        }

        T v = L.meet(currentValue, currentNode.value);
        result.value = L.join(result.value, v);
        //System.out.println(currentNode.upperLinks.size());
        for (Link l : currentNode.upperLinks) {
            if (l.target.lastSeen != result) {
                v = currentValue;
                for (Integer i : l.diff) {
                    v = L.meet(v, input.get(i));
                }
                recMinOutput(result, l.target, v, input);
            }
        }
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
