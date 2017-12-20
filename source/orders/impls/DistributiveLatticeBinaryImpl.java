/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import orders.DistributiveLattice;
import orders.Poset;

/**
 *
 * @author qgbrabant
 */
public class DistributiveLatticeBinaryImpl extends HashSet<BTupleImpl> implements Poset<BTupleImpl>, DistributiveLattice<BTupleImpl> {

    private final BTupleImpl top;
    private final BTupleImpl bottom;
    private final int dimension;
    private BTupleImpl[] scp;
    private BTupleImpl[] scm;

    public DistributiveLatticeBinaryImpl(Set<BTupleImpl> elements, int dimension) {
        super();
        this.addAll(elements);
        this.dimension = dimension;
        Boolean[] binTop = new Boolean[dimension];
        Boolean[] binBottom = new Boolean[dimension];
        for (int i = 0; i < dimension; i++) {
            binTop[i] = true;
            binBottom[i] = false;
        }
        this.top = new BTupleImpl(binTop);
        this.bottom = new BTupleImpl(binBottom);
        this.setSCs();
    }

    public DistributiveLatticeBinaryImpl(List<BTupleImpl> jointIrreducibles) {
        super();

        this.dimension = jointIrreducibles.get(0).size();

        Boolean[] binTop = new Boolean[dimension];
        Boolean[] binBottom = new Boolean[dimension];
        
        for (int i = 0; i < dimension; i++) {
            binTop[i] = false;
            binBottom[i] = true;
            for (BTupleImpl ji : jointIrreducibles) {
                binTop[i] |= ji.get(i);
                binBottom[i] &= ji.get(i);
            }
        }
        
        Set<BTupleImpl> news = new HashSet<>(jointIrreducibles);
        Set<BTupleImpl> news2;
        while(this.addAll(news)){
            news2 = new HashSet<>();
            for (BTupleImpl b : this) {
                for(BTupleImpl b2 : news){
                    news2.add(this.join(b,b2));
                } 
            }
            news = news2;
        }
        this.top = new BTupleImpl(binTop);
        this.bottom = new BTupleImpl(binBottom);
        this.setSCs();
    }

    private void setSCs() {
        this.scp = new BTupleImpl[this.dimension];
        this.scm = new BTupleImpl[this.dimension];
        Integer r;
        for (int i = 0; i < this.dimension; i++) {
            this.scp[i] = this.top;
            this.scm[i] = this.bottom;
        }

        for (BTupleImpl x : this) {
            for (int i = 0; i < this.dimension; i++) {
                if (x.get(i)) {
                    r = x.relation(scp[i]);
                    if (r != null && r == 1) {
                        this.scp[i] = x;
                    }
                }
                if (!x.get(i)) {
                    r = x.relation(scm[i]);
                    if (r != null && r == -1) {
                        this.scm[i] = x;
                    }
                }
            }
        }
    }

    @Override
    public BTupleImpl join(BTupleImpl x, BTupleImpl y) {
        Boolean[] bin = new Boolean[this.dimension];
        for(int i = 0 ; i < this.dimension ; i ++){
            bin[i] = x.get(i) || y.get(i);
    }
        return new BTupleImpl(bin);
    }

    @Override
    public BTupleImpl meet(BTupleImpl x, BTupleImpl y) {
        Boolean[] bin = new Boolean[this.dimension];
        for(int i = 0 ; i < this.dimension ; i ++){
            bin[i] = x.get(i) && y.get(i);
    }
        return new BTupleImpl(bin);
    }

    @Override
    public BTupleImpl getTop() {
        return this.top;
    }

    @Override
    public BTupleImpl getBottom() {
        return this.bottom;
    }

    @Override
    public Integer relation(BTupleImpl x, BTupleImpl y) {
        return x.relation(y);
    }

    @Override
    public int getDimensionality() {
        return dimension;
    }

    @Override
    public BTupleImpl getStPlus(int i) {
        return this.scp[i];
    }

    @Override
    public BTupleImpl getStMinus(int i) {
        return this.scm[i];
    }

    public String fancyToString() {
        List<Set<BTupleImpl>> floors = this.getFloors();
        String res = "";
        for (int i = dimension; i >= 0; i--) {
            res += floors.get(i) + "\n";
        }
        return res;
    }

    public List<Set<BTupleImpl>> getFloors() {
        List<Set<BTupleImpl>> floors = new ArrayList<>();
        for (int i = 0; i <= this.dimension; i++) {
            floors.add(new HashSet<>());
        }
        int d;
        for (BTupleImpl bt : this) {
            d = 0;
            for (int i = 0; i < this.dimension; i++) {
                if (bt.get(i)) {
                    d++;
                }
            }
            floors.get(d).add(bt);
        }
        return floors;
    }

    @Override
    public List<BTupleImpl> getJointIrreducibles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BTupleImpl randomValue() {
        int item = ThreadLocalRandom.current().nextInt(0, this.size());
        int i = 0;
        for (BTupleImpl obj : this) {
            if (i == item) {
                return obj;
            }
            i++;
        }
        return null;
    }

    @Override
    public BTupleImpl randomValueBetween(BTupleImpl min, BTupleImpl max) {
        HashSet<BTupleImpl> s = new HashSet<>();
        Integer r;
        for (BTupleImpl obj : this) {
            r = obj.relation(min);
            if (r != null && r <= 0 ) {
                r = obj.relation(max);
                if (r != null && r >= 0) {
                    s.add(obj);
                }
            }
        }
        assert s.size() > 0 : "min : " + min + ", max : " + max;
        int item = ThreadLocalRandom.current().nextInt(0, s.size());
        int i = 0;
        for (BTupleImpl obj : s) {
            if (i == item) {
                return obj;
            }
            i++;
        }
        return null;
    }

}
