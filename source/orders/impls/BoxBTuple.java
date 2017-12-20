/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

/**
 *
 * @author qgbrabant
 */
public class BoxBTuple<G> extends BTupleImpl {
    private G content;

    public BoxBTuple(Boolean[] bin, G c) {
        super(bin);
        this.content =c;
    }
    
    public BoxBTuple(Boolean[] bin) {
        super(bin);
    }
    
    public void setContent(G c){
        this.content = c;
    }
    
    public G getContent(){
        return content;
    }
}
