/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import miscellaneous.Misc;
import orders.Poset;
import orders.SemiLattice;

/**
 *
 * @author qgbrabant
 */
public class SemiLatticeBinaryImpl extends HashSet<BTupleImpl> implements Poset<BTupleImpl>, SemiLattice<BTupleImpl> {

    private final BTupleImpl bottom;
    private final int dimension;

    public SemiLatticeBinaryImpl(Set<BTupleImpl> elements, int dimension) {
        super();
        this.addAll(elements);
        this.dimension = dimension;
        Boolean[] binBottom = new Boolean[dimension];
        for (int i = 0; i < dimension; i++) {
            binBottom[i] = false;
        }
        this.bottom = new BTupleImpl(binBottom);
    }

    public SemiLatticeBinaryImpl(File f) throws FileNotFoundException, IOException {
        super();
        Set<BTupleImpl> elements = new HashSet<>();

        BufferedReader fileReader = null;

        String line = "";
        
        
        
        fileReader = new BufferedReader(new FileReader(f));
        this.dimension = Misc.countLines(f);
        Boolean[] bins = new Boolean[dimension];
        Arrays.fill(bins, Boolean.TRUE);
        BTupleImpl b = new BTupleImpl(bins);
        while ((line = fileReader.readLine()) != null) {
            Arrays.fill(bins, Boolean.FALSE);
            String[] tokens = line.split(";");
            for (String id : tokens[1].split(",")) {
                bins[parseInt(id)] = true;
            }
            BTupleImpl hey = new BTupleImpl(bins);
            //System.out.println(hey);
            elements.add(hey);
            b = this.meet(b,hey);
        }
        fileReader.close();

        Boolean[] binBottom = new Boolean[dimension];
        for (int i = 0; i < dimension; i++) {
            binBottom[i] = false;
        }
        this.bottom = b;
        elements.add(b);
        this.addAll(elements);
    }

    @Override
    public BTupleImpl meet(BTupleImpl x, BTupleImpl y) {
        Boolean[] bin = new Boolean[this.dimension];
        for (int i = 0; i < this.dimension; i++) {
            bin[i] = x.get(i) && y.get(i);
        }
        return new BTupleImpl(bin);
    }

    @Override
    public BTupleImpl getBottom() {
        return this.bottom;
    }

    @Override
    public Integer relation(BTupleImpl x, BTupleImpl y) {
        int res = 0;
        int r;
        if (x == y) {
            return 0;
        }
        for (int i = 0; i < this.dimension; i++) {
            r = 0;
            if (x.get(i) && !y.get(i)) {
                r = -1;
            }
            if (!x.get(i) && y.get(i)) {
                r = -1;
            }
            if (r != res) {
                if (r == -res) {
                    return null;
                } else {
                    res = r;
                }
            }
        }
        return res;
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

}
