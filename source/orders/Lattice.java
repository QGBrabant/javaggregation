/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders;

/**
 *
 * @author qgbrabant
 * @param <T>
 */
public interface Lattice<T> extends SemiLattice<T> {
    public T join(T x, T y);
    public T getTop();
    
    /**
     * 
     * @param min
     * @param max
     * @return a value between min and max (uniform probability).
     */
    public T randomValueBetween(T min, T max);
}
