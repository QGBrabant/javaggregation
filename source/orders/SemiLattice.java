/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders;

/**
 * Bottom semi-lattice.
 * @author qgbrabant
 */
public interface SemiLattice<T> extends Order<T> {
    public T meet(T x, T y);
    public T getBottom();
    public int getDimensionality();
}
