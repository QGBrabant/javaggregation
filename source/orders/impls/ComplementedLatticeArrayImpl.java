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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import miscellaneous.IndexManager;
import orders.ComplementedLattice;
import orders.DistributiveLattice;
import orders.Poset;

/**
 *
 * @author qgbrabant
 */
public class ComplementedLatticeArrayImpl extends HashSet<IndexedBTuple> implements Poset<IndexedBTuple>, DistributiveLattice<IndexedBTuple>, ComplementedLattice<IndexedBTuple> {

    private final IndexedBTuple top;
    private final IndexedBTuple bottom;
    private final int dimension;
    private final IndexedBTuple[] scp;
    private final IndexedBTuple[] scm;
    private final IndexedBTuple[][] theGreatArrayOfSupremumPrecomputation;
    private final IndexedBTuple[][] theGreatArrayOfInfimumPrecomputation;
    private final IndexedBTuple[] theGreatArrayOfComplementsPrecomputation;
    private final List<IndexedBTuple> shuffledList;

    public <U extends BTupleImpl, T extends DistributiveLattice<U> & Set<U>> ComplementedLatticeArrayImpl(T L) {
        super();

        this.dimension = L.getDimensionality();

        shuffledList = new LinkedList<>();
        Map<BTupleImpl, IndexedBTuple> theMap = new HashMap<>();
        IndexManager.IndexGroupKey igk = IndexManager.createIndexGroupKey();
        IndexedBTuple t = null;
        IndexedBTuple b = null;
        List<BTupleImpl> tlist = new ArrayList<>(L);
        Collections.shuffle(tlist);

        IndexedBTuple comp;
        Boolean[] reversed = new Boolean[this.dimension];
        for (BTupleImpl bt : tlist) {
            IndexedBTuple ibt = new IndexedBTuple(bt.getBin(), igk.nextIndex());
            theMap.put(ibt, ibt); // This may look silly, but really... idk.
            this.add(ibt);

            if (bt.equals(L.getTop())) {
                assert t == null;
                t = ibt;
            }
            if (bt.equals(L.getBottom())) {
                assert b == null;
                b = ibt;
            }
            
            
            
            for (int i = 0; i < this.dimension; i++) {
                reversed[i] = !ibt.get(i);
            }
            comp = theMap.get(new BTupleImpl(reversed));
            if (comp == null) {
                comp = new IndexedBTuple(reversed, igk.nextIndex());
                theMap.put(comp, comp);
                this.add(comp);
            }
        }
        
        this.theGreatArrayOfComplementsPrecomputation = new IndexedBTuple[this.size()+1];

        //Linking complements together
        for (IndexedBTuple bt : this) {
            comp = theMap.get(new BTupleImpl(reversed));
            this.theGreatArrayOfComplementsPrecomputation[bt.getIndex()] = comp;
        }

        this.top = t;
        this.bottom = b;
        this.scp = new IndexedBTuple[dimension];
        this.scm = new IndexedBTuple[dimension];
        for (int i = 0; i < dimension; i++) {
            scp[i] = theMap.get(L.getStPlus(i)); // Are you impressed?
            scm[i] = theMap.get(L.getStMinus(i));
        }

        
        Boolean[] bin = new Boolean[this.dimension];
        theGreatArrayOfSupremumPrecomputation = new IndexedBTuple[this.size()+1][this.size()+1];
        theGreatArrayOfInfimumPrecomputation = new IndexedBTuple[this.size()+1][this.size()+1];

        for (IndexedBTuple ibt1 : this) {

            this.shuffledList.add(ibt1);
            for (IndexedBTuple ibt2 : this) {
                for (int i = 0; i < this.dimension; i++) {
                    bin[i] = ibt1.get(i) || ibt2.get(i);
                }
                theGreatArrayOfSupremumPrecomputation[ibt1.getIndex()][ibt2.getIndex()] = theMap.get(new BTupleImpl(bin));

                for (int i = 0; i < this.dimension; i++) {
                    bin[i] = ibt1.get(i) && ibt2.get(i);
                }
                theGreatArrayOfInfimumPrecomputation[ibt1.getIndex()][ibt2.getIndex()] = theMap.get(new BTupleImpl(bin));
            }
        }
    }

    @Override
    public IndexedBTuple getComplement(IndexedBTuple ibt) {
        return this.theGreatArrayOfComplementsPrecomputation[ibt.getIndex()];
    }

    @Override
    public int getDimensionality() {
        return dimension;
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
    public IndexedBTuple getStPlus(int i) {
        return this.scp[i];
    }

    @Override
    public IndexedBTuple getStMinus(int i) {
        return this.scm[i];
    }

    @Override
    public List<IndexedBTuple> getJointIrreducibles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IndexedBTuple join(IndexedBTuple x, IndexedBTuple y) {
        return this.theGreatArrayOfSupremumPrecomputation[x.getIndex()][y.getIndex()];
    }

    @Override
    public IndexedBTuple getTop() {
        return this.top;
    }

    @Override
    public IndexedBTuple randomValue() {
        int item = ThreadLocalRandom.current().nextInt(0, this.size());
        int i = 0;
        for (IndexedBTuple obj : this) {
            if (i == item) {
                return obj;
            }
            i++;
        }
        return null;
    }

    /*@Override
    public IndexedBTuple randomValueBetween(BTuple min, BTuple max) {
        HashSet<IndexedBTuple> s = new HashSet<>();
        Integer r;
        for (IndexedBTuple obj : this) {
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
        for (IndexedBTuple obj : s) {
            if (i == item) {
                return obj;
            }
            i++;
        }
        return null;
    }*/
    @Override
    public IndexedBTuple randomValueBetween(IndexedBTuple min, IndexedBTuple max) {
        int n = this.size();
        int i;
        Integer r;
        IndexedBTuple imin = (IndexedBTuple) min;
        IndexedBTuple imax = (IndexedBTuple) max;
        while (true) {
            i = ThreadLocalRandom.current().nextInt(0, n);
            IndexedBTuple ibt = this.shuffledList.get(i);
            r = this.relation(ibt, imin);
            if (r != null && r <= 0) {
                r = this.relation(ibt, imax);
                if (r != null && r >= 0) {
                    return ibt;
                }
            }
            this.shuffledList.add(ibt);
            this.shuffledList.remove(i);
            n--;
        }
    }

    @Override
    public IndexedBTuple meet(IndexedBTuple x, IndexedBTuple y) {
        return this.theGreatArrayOfInfimumPrecomputation[x.getIndex()][y.getIndex()];
    }

    @Override
    public IndexedBTuple getBottom() {
        return this.bottom;
    }

    @Override
    public Integer relation(IndexedBTuple x, IndexedBTuple y) {
        IndexedBTuple m = this.meet(x, y);
        if (m.getIndex() == x.getIndex()) {
            if (m.getIndex() == y.getIndex()) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (m.getIndex() == y.getIndex()) {
                return -1;
            } else {
                return null;
            }
        }
    }
}
