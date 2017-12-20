/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import miscellaneous.IndexManager;
import miscellaneous.IndexManager.IndexGroupKey;
import orders.Poset;
import orders.SemiLattice;

/**
 *
 * @author qgbrabant
 */
public class SemiLatticeArrayImpl extends HashSet<IndexedBTuple> implements Poset<IndexedBTuple>, SemiLattice<IndexedBTuple> {

    private final IndexedBTuple bottom;
    private final int dimension;
    private final IndexedBTuple[][] theGreatArrayOfInfimumPrecomputation;

    public <U extends BTupleImpl, T extends SemiLattice<U> & Set<U>> SemiLatticeArrayImpl(T L) {
        super();
        Map<BTupleImpl, IndexedBTuple> theMap = new HashMap<>();
        IndexGroupKey igk = IndexManager.createIndexGroupKey();
        IndexedBTuple b = null;
        for (BTupleImpl bt : L) {
            IndexedBTuple ibt = new IndexedBTuple(bt.getBin(), igk.nextIndex());
            theMap.put(ibt, ibt); // This may look silly, but really... idk, may be not.
            this.add(ibt);
            if (bt.equals(L.getBottom())) {
                assert b == null;
                b = ibt;
            }
        }
        this.bottom = b;
        this.dimension = L.getDimensionality();

        Boolean[] bin = new Boolean[this.dimension];
        theGreatArrayOfInfimumPrecomputation = new IndexedBTuple[this.size()][this.size()];

        this.forEach((ibt1) -> {
            for (IndexedBTuple ibt2 : this) {
                for (int i = 0; i < this.dimension; i++) {
                    bin[i] = ibt1.get(i) && ibt2.get(i);
                }
                theGreatArrayOfInfimumPrecomputation[ibt1.getIndex()][ibt2.getIndex()] = theMap.get(new BTupleImpl(bin));
            }
        });

    }
    
    public SemiLatticeArrayImpl(File f) throws IOException{
        this(new SemiLatticeBinaryImpl(f));
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
        return x.relation(y);
    }
}
