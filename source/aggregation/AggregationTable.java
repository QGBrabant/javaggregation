/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aggregation;

import java.util.Map.Entry;
import java.util.Set;
import miscellaneous.TupleImpl;
import orders.BTuple;
import orders.DistributiveLattice;
import ordinalclassification.WrongArityException;

/**
 * Represents a partial function from L^n to L, where L is a distributive lattice.
 * @author qgbrabant
 * @param <T> type of the elements in L
 */
public abstract class AggregationTable <T extends BTuple> {
    private final int arity;
    private final DistributiveLattice<T> space;
    public abstract void put(TupleImpl<T> input, T output);
    
    public AggregationTable(DistributiveLattice<T> dlattice, int arity){
        this.space = dlattice;
        this.arity = arity;
    }
    
    public final T getOutput(TupleImpl<T> input, int i) throws WrongArityException{
        if(input.size() != this.arity){
            throw new WrongArityException();
        }
        return this.getOutPutImpl(input,i);
    }
    
    protected abstract T getOutPutImpl(TupleImpl<T> x, int i);
    
    public int getArity(){
        return arity;
    }
    
    public DistributiveLattice<T> getSpace(){
        return this.space;
    }
    
    /**
     * Returns all the (input,output) contained in the AggregationTable
     * @return 
     */
    public abstract Set<Entry<Input,T>> pointSet();
    
    public class Input {

        private final TupleImpl<T> input;
        private final int number;

        public Input(TupleImpl<T> t, int n) {
            this.input = t;
            this.number = n;
        }

        public TupleImpl<T> getInput() {
            return this.input;
        }

        public int getNumber() {
            return this.number;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof AggregationTable.Input)) {
                return false;
            }
            return (this.number == ((AggregationTable.Input) o).getNumber())
                    && (this.input.equals(((AggregationTable.Input) o).getInput()));
        }

        @Override
        public int hashCode() {
            return this.input.hashCode() ^ (new Integer(this.number).hashCode());
        }

        public String toString() {
            return this.input + ", n=" + this.number;
        }
    }
    
}
