/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aggregation;

import orders.impls.BTupleImpl;
import orders.impls.BoxBTuple;

/**
 *
 * @author qgbrabant
 */
public class CapacityNode<T extends BTupleImpl> extends BoxBTuple<T>{

    public CapacityNode(Boolean[] bin, T c) {
        super(bin, c);
        assert c instanceof BTupleImpl;
    }
   
    
    @Override
    public Integer relation(Object o) {    
        if(!(o instanceof BTupleImpl)){
            assert false;
            return null;
        }
        
        CapacityNode<BTupleImpl> x = (CapacityNode) o;
        Integer r1 = 0;

        for(int i = 0; i < this.size() ; i ++){
            if(this.get(i) && !x.get(i)){
                if(r1 == 1){
                    return null;
                }else{
                    r1 = -1;
                }
            }else if (!this.get(i) && x.get(i)){
                if(r1 == -1){
                    return null;
                }else{
                    r1 = 1;
                }
            }
        }
        Integer r2 = this.getContent().relation(x.getContent());
        if(r2 != null && r2 != r1 ){
            return r1;
        }
        return null;
    }
    
    public T getConstraint(){
        return (T) this.getContent();
    }
}
