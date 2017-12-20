/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aggregation;

import java.util.List;
import java.util.function.Function;



/**
 *
 * @author qgbrabant
 * @param <T> type of the elements to aggregate
 */
public interface AggregationFunction <T> extends Function<List<T>,T>{
    public T apply(T[] x);
    public int getArity();
    
}
