/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import orders.Chain;

/**
 *
 * @author qgbrabant
 */
public interface ChainBuilder {
    public boolean add(Double threshold);
    public Lexicon buildChain();
    
    public static interface Lexicon {
        public Rank get(double d);
        public Chain getChain();
    }
}
