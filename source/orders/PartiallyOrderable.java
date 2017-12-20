/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders;

/**
 * 
 * @author qgbrabant
 */
public interface PartiallyOrderable {
    /**
     * 
     * @param o
     * @return 1 if this < o, -1 if o < this, 0 if o == this, null otherwise.
     */
    public Integer relation(Object o);

}
