/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders;

import java.util.Set;

/**
 *
 * @author qgbrabant
 * @param <T>
 */
public interface Poset<T> extends Set<T>, Order<T>{
    /**
     * 
     * @return a random element from the order (uniform probability).
     */
    public T randomValue();

}
