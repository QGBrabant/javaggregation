/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscellaneous;

/**
 * 
 * @author qgbrabant
 */
public class Couple<T> {
    private final T left;
    private final T right;
    
    public Couple(T l, T r){
        this.left = l;
        this.right =r;
    }
    
    public T getLeft(){
        return left;
    }
    
    public T getRight(){
        return right;
    }
    
    @Override
    public String toString(){
        return left+" & "+right;
    }
}
