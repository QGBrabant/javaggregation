/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aggregation;

import miscellaneous.TupleImpl;
import orders.BTuple;
import orders.DistributiveLattice;

/**
 *
 * @author qgbrabant
 */
public interface Capacity<T extends BTuple> {
    
    public int getArity();
    public DistributiveLattice<T> getCodomain();
    public T getSugenoOutput(TupleImpl<T> input);
    
    
}
