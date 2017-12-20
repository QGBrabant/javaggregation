/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscellaneous;

import java.io.Serializable;

/**
 *
 * @author qgbrabant
 */
public interface Tuple <T> extends Serializable {
    
    /**
     * @param i
     * @return the ith element of the tuple.
     */
    public T get(int i);
    
    /**
     * @return length of the tuple
     */
    public int size();
    
    /**
     * @return an array that contains the same references as the tuple (same length, same order).
     */
    public T[] toArray();
}