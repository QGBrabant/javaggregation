/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import java.util.ArrayList;
import java.util.Arrays;
import org.json.simple.JSONArray;

/**
 *
 * @author qgbrabant
 */
public class IndexedSparseBTuple extends IndexedBTuple {

    private final int[] indices;
    private final int dimensionality;

    public IndexedSparseBTuple(Boolean[] bin, int index) {
        super(bin, index);
        throw new UnsupportedOperationException("Don't do this.");
    }

    public IndexedSparseBTuple(int[] indices, int dim, int index) {
        super(new Boolean[]{}, index);
        this.indices = Arrays.copyOf(indices, indices.length);
        this.dimensionality = dim;
        this.precomputeH();
        this.precomputeRep();
    }

    public IndexedSparseBTuple(ArrayList<Integer> lindices, int dim, int index) {
        super(new Boolean[]{}, index);
        this.indices = new int[lindices.size()];
        for (int i = 0; i < this.indices.length; i++) {
            this.indices[i] = lindices.get(i);
        }
        this.dimensionality = dim;
        this.precomputeH();
        this.precomputeRep();
    }

    public int[] getIndices() {
        return this.indices;
    }

    @Override
    protected void precomputeRep() {
        this.rep = Arrays.toString(this.indices);
    }

    @Override
    protected void precomputeH() {
        int result = 1;
        for (int i = 0; i < this.dimensionality; i++) {
            result = 31 * result + (this.get(i) ? 1231 : 1237);
        }
        this.H = result;
    }

    @Override
    public Boolean get(int i) {
        return Arrays.binarySearch(this.indices, i) >= 0;
    }

    @Override
    public int size() {
        return this.dimensionality;
    }

    @Override
    public boolean equals(Object o) {
        if (this.hashCode() == o.hashCode()) {
            if (o instanceof IndexedSparseBTuple) {
                IndexedSparseBTuple other = (IndexedSparseBTuple) o;
                if (other.indices.length == this.indices.length) {
                    for (int i = 0; i < other.indices.length; i++) {
                        if (other.indices[i] != this.indices[i]) {
                            return false;
                        }
                    }
                    return true;
                }
            } else {
                System.out.println("Warning: comparison between sparse and nonsparse tuple.");
                BTupleImpl other = (BTupleImpl) o;
                if (other.size() == this.size()) {
                    for (int i = 0; i < size(); i++) {
                        if (other.get(i) != this.get(i)) {
                            return false;
                        }
                    }
                    return true;
                }
            }

        }
        return false;
    }

    public String toString() {
        return rep;
    }

    public Boolean[] toArray() {
        Boolean[] t = (Boolean[]) new Object[this.size()];
        for (int i = 0; i < this.size(); i++) {
            t[i] = this.get(i);
        }
        return t;
    }

    @Override
    public Integer relation(Object o) {
        if (!(o instanceof BTupleImpl)) {
            assert false;
            return null;
        }
        if (!(o instanceof IndexedSparseBTuple)) {
            assert false;
            return ((BTupleImpl) o).relation(this);
        }
        IndexedSparseBTuple other = (IndexedSparseBTuple) o;

        int i = 0;
        int j = 0;
        boolean sup = this.indices.length >= other.indices.length;
        boolean inf = this.indices.length <= other.indices.length;

        while ((sup || inf) && i < this.indices.length && j < other.indices.length) {
            //System.out.println("i:"+i+", j:"+j);
            if (this.indices[i] == other.indices[j]) {
                i++;
                j++;
                
            } else if (this.indices[i] > other.indices[j]) {
                j++;
                sup = false;
            } else {
               i++;
                inf = false;
            }
            if(j >= other.indices.length && !(i >= this.indices.length)){
                    inf = false;
                }
                if(i >= this.indices.length && !(j >= other.indices.length)){
                    sup = false;
                } 
        }
        
        if(sup && inf) return 0;
        if(sup) return -1;
        if(inf) return 1;
        return null;
    }

    public JSONArray exportJSON() {
        JSONArray a = new JSONArray();
        for (Integer i : this.indices) {
            a.add(i);
        }
        return a;
    }
}
