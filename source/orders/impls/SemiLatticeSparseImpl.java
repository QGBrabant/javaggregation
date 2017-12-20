/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import miscellaneous.IndexManager;
import miscellaneous.IndexManager.IndexGroupKey;
import miscellaneous.Misc;
import orders.Poset;
import orders.SemiLattice;

/**
 *
 * @author qgbrabant
 */
public class SemiLatticeSparseImpl extends HashSet<SemiLatticeSparseImpl.Element> implements Poset<SemiLatticeSparseImpl.Element>, SemiLattice<SemiLatticeSparseImpl.Element> {
    
    public class Element extends IndexedSparseBTuple{
        private Element upperGuy;
        private String name;
        
        /*public Element(ArrayList<Integer> lindices, int dim, int index) {
            super(lindices, dim, index);
            this.upperGuy = this;
            this.name = rep;
        }*/
        
        public Element(ArrayList<Integer> lindices, int dim, int index, String name) {
            super(lindices, dim, index);
            this.upperGuy = this;
            this.name = name;
        }
        
        public Element getUpperGuy(){
            return this.upperGuy;
        }
        
        /*public String toString(){
            return this.name;
        }*/
    }

    private final Element bottom;
    private final int dimension;
    private final Element[][] theGreatArrayOfInfimumStorage;
    private final Map<ArrayList<Integer>, Element> introspectionMap;
    private final Map<String,Element> dictionary;


    public SemiLatticeSparseImpl(File f) throws IOException {
        super();
        System.out.println("SemiLatticeSparseImpl creation...");
        
        IndexGroupKey igk = IndexManager.createIndexGroupKey();
        
        this.introspectionMap = new HashMap<>();
        this.dictionary = new HashMap<>();

        System.out.println("Reading file...");
        ArrayList<Integer> indices;

        Element isbt;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(f))) {
            this.dimension = Misc.countLines(f);
            //int[] featureCount = new int[this.dimension];
            //Arrays.fill(featureCount, 0);
            indices = new ArrayList<>();
            for (int i = 0; i < this.dimension; i++) {
                indices.add(i);
            }   indices = new ArrayList<>();
            int blabla;
            String[] splittedLine;
            splittedLine = fileReader.readLine().split(";");
            for (String id : splittedLine[1].split(",")) {
                blabla = parseInt(id);
                indices.add(blabla);
                //featureCount[blabla]++;
            }
            indices.sort(Integer::compareTo);
            this.bottom = new Element(indices, this.dimension, igk.nextIndex(),splittedLine[0]);
            this.bottom.upperGuy = this.bottom;
            this.introspectionMap.put(indices, this.bottom);
            String line;
            int numline = 0;
            while ((line = fileReader.readLine()) != null) {
                indices = new ArrayList<>();
                String[] tokens = line.split(";");
                for (String id : tokens[1].split(",")) {
                    blabla = parseInt(id);
                    indices.add(blabla);
                    //featureCount[blabla]++;
                }
                indices.sort(Integer::compareTo);
                if (!this.introspectionMap.containsKey(indices)) {
                    isbt = new Element(indices, this.dimension, igk.nextIndex(),tokens[0]);
                    isbt.upperGuy = isbt;
                    this.introspectionMap.put(indices, isbt);
                }   numline++;
            }
        }
        this.addAll(introspectionMap.values());

        System.out.println("Removing leaves...");
        List<Element> candidates = new ArrayList<>();
        boolean keep;
        for (Element x : this) {
            keep = true;
            indices = new ArrayList();
            for (int i : x.getIndices()) {
                //if (featureCount[i] <= 1) {
                //    keep = false;
                //} else {
                    indices.add(i);
                //}
            }
            if (keep) {
                candidates.add((Element)x);
                x.upperGuy = x;
                x.setIndex(candidates.size() - 1);
            } else {
                assert false;
                x.upperGuy = this.introspectionMap.get(indices);
                if (x.upperGuy == null) {
                    isbt = new Element(indices,
                            this.dimension,
                            igk.nextIndex(),
                            "!TODO!"
                            );
                    this.introspectionMap.put(indices, isbt);
                    candidates.add(isbt);
                    isbt.setIndex(candidates.size() - 1);
                    isbt.upperGuy = isbt;
                    x.upperGuy = isbt;
                }

                x.setIndex(-1);
                assert x.upperGuy != null;
            }
        }

        int arraysSize = candidates.size();

        System.out.println("Completing lattice...");

        List<Element> toAdd;
        while (!candidates.isEmpty()) {
            toAdd = new ArrayList();
            System.out.println("" + candidates.size() + " candidates.");
            int nbloops = 0;
            for (Element a : candidates) {
                //System.out.println(nbloops + "/" + candidates.size());
                for (Element b : candidates) {
                    indices = this.sparseMeet(a, b);
                    if (!this.introspectionMap.containsKey(indices)) {
                        isbt = new Element(indices, this.dimension, igk.nextIndex(),"!TODO!");
                        toAdd.add(isbt);
                        this.introspectionMap.put(indices, isbt);
                        isbt.upperGuy = isbt;
                        /*a.upperGuy = isbt;
                        b.upperGuy = isbt;*/
                        this.add(isbt);
                        isbt.setIndex(arraysSize);
                        arraysSize++;
                    }
                }
                nbloops++;
            }
            candidates = toAdd;
        }

        System.out.println("Initializing arrays...");
        theGreatArrayOfInfimumStorage = new Element[arraysSize][arraysSize];
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
    public Element randomValue() {
        int item = ThreadLocalRandom.current().nextInt(0, this.size());
        int i = 0;
        for (Element obj : this) {
            if (i == item) {
                return obj;
            }
            i++;
        }
        return null;
    }

    @Override
    public Element meet(Element x, Element y) {
        if (x == y) return x;
        int i = x.upperGuy.getIndex();
        int j = y.upperGuy.getIndex();
        Element ibt = this.theGreatArrayOfInfimumStorage[i][j];
        if (ibt == null) {
            ibt = this.introspectionMap.get(this.sparseMeet(x.upperGuy, y.upperGuy));
            this.theGreatArrayOfInfimumStorage[i][j] = ibt;
            this.theGreatArrayOfInfimumStorage[j][i] = ibt;
        }
        return ibt;
    }

    final public ArrayList<Integer> sparseMeet(IndexedSparseBTuple x, IndexedSparseBTuple y) {
        ArrayList<Integer> l = new ArrayList<>();
        int i = 0;
        int j = 0;
        int[] tabx = x.getIndices();
        int[] taby = y.getIndices();
        while (i < tabx.length && j < taby.length) {
            if (tabx[i] > taby[j]) {
                j++;
            } else if (tabx[i] < taby[j]) {
                i++;
            } else {
                l.add(tabx[i]);
                i++;
                j++;
            }
        }
        return l;
    }

    @Override
    public Element getBottom() {
        return this.bottom;
    }

    @Override
    public Integer relation(Element x, Element y) {
        return x.relation(y);
    }

    public Element getFromSparseRep(ArrayList<Integer> rep) {
        return this.introspectionMap.get(rep);
    }
    
    public Element getElement(List<Integer> l){
        return this.introspectionMap.get(l);
    }
    
    
}
