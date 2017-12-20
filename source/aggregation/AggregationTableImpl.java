/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aggregation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import miscellaneous.DefaultHashMap;
import miscellaneous.Lexicon;
import miscellaneous.NotInLexiconException;
import miscellaneous.TupleImpl;
import orders.BTuple;
import orders.DistributiveLattice;
import orders.impls.BTupleImpl;

/**
 * Implementation of AggregationTable
 *
 * @author qgbrabant
 */
public class AggregationTableImpl<T extends BTuple> extends AggregationTable<T> {

    private final DistributiveLattice<? extends T> space;
    private final Map<Input, T> map;
    private final DefaultHashMap<TupleImpl<T>, Integer> countMap;

    /**
     * Generates and returns a random instance of AggregationTableImpl. Each
     * couple (input,output) is added to the table by randomly picking the
     * input. The output is given by the capacity mu.
     *
     * @param <T> type of value returned by mu
     * @param size number of values in the table
     * @param mu the capacity with which the table is "filled"
     * @return a random instance of AggregationTableImpl
     */
    public static <T extends BTuple> AggregationTable<T> randomInstance(int size, Capacity<T> mu) {
        AggregationTable<T> ds = new AggregationTableImpl<>(mu.getCodomain(), mu.getArity());
        T[] inputRow = (T[]) new BTupleImpl[mu.getArity()];
        TupleImpl<T> input;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < mu.getArity(); j++) {
                inputRow[j] = mu.getCodomain().randomValue();
            }
            input = new TupleImpl<>(inputRow);
            ds.put(input, mu.getSugenoOutput(input));
        }
        return ds;
    }

    public AggregationTableImpl(DistributiveLattice<T> dlattice, int arity) {
        super(dlattice, arity);
        this.map = new HashMap<>();
        this.countMap = new DefaultHashMap<>(0);
        this.space = dlattice;
    }

    public AggregationTableImpl(DistributiveLattice<T> dlattice, int arity, Lexicon lex, File f, int limit) {
        super(dlattice, arity);
        this.space = dlattice;
        this.map = new HashMap<>();
        this.countMap = new DefaultHashMap<>(0);
        int i = 0;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(f));
            List<T> sequence;
            while (((line = br.readLine()) != null) && i != limit) {
                try {

                    sequence = lex.lineToList(line);

                    T output = sequence.get(sequence.size() - 1);
                    sequence.remove(sequence.size() - 1);
                    TupleImpl<T> t = new TupleImpl<>(sequence);

                    this.put(t, output);

                } catch (NotInLexiconException ex) {
                    System.out.println("Encountered unknown value name (not in lexicon), line skipped.");
                    Logger.getLogger(AggregationTableImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AggregationTableImpl(Lexicon lex, File f, DistributiveLattice<T> dlattice, int arity) {
        this(dlattice, arity, lex, f, -1);
    }

    @Override
    public void put(TupleImpl<T> input, T output) {
        int c = this.countMap.defaultGet(input) + 1;
        this.countMap.put(input, c);
        this.map.put(new Input(input, c), output);
    }

    @Override
    public String toString() {
        String s = "NAryAggregationDatasetImpl : {";
        Input k;
        for (Entry<TupleImpl<T>, Integer> e : this.countMap.entrySet()) {
            for (int i = 1; i <= e.getValue(); i++) {
                k = new Input(e.getKey(), i);
                s += k + " -> " + this.map.get(k) + "\n";
            }
        }
        s += "}";
        return s;
    }

    @Override
    public Set<Entry<Input, T>> pointSet() {
        return this.map.entrySet();
    }

    @Override
    protected T getOutPutImpl(TupleImpl<T> x, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
