/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import miscellaneous.Indexable;

/**
 *
 * @author qgbrabant
 */
public class IndexedBTuple extends BTupleImpl implements Indexable{
    private int index;

    public IndexedBTuple(Boolean[] bin, int index) {
        super(bin);
        this.index = index;
    }
    
    @Override
    public int getIndex(){
        return this.index;
    }
    
    @Override
    public Object join(Object o) {
        assert false;
        return null;
    }
    
    @Override
    public Object meet(Object o) {
        assert false;
        return null;
    }
    
    @Override
    public void setIndex(int i){
        this.index = i ;
    }
}
