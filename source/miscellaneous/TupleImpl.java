/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscellaneous;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author qgbrabant
 */
public class TupleImpl <T> implements Tuple<T>{
    protected final T[] array;
    protected int H;
    protected String rep;
    
    public TupleImpl(T... a){
        super();
        this.array= Arrays.copyOf(a, a.length);

        this.precomputeH();
        this.precomputeRep();
    }
    
    public TupleImpl(List<T> a){
        super();
        int l = 0;
        for(T x : a){
            l++;
        }
        this.array=(T[]) new Object[l];
        for(int i = 0; i < a.size() ; i ++){
            this.array[i] = a.get(i);
        }
        this.precomputeH();
        this.precomputeRep();
        
    }
    
    protected void precomputeH(){
        H = java.util.Arrays.deepHashCode(this.array);
    }
    
    protected void precomputeRep(){
        String s = "(";
        for (T array1 : this.array) {
            if (array1 instanceof Boolean) {
                s += (Boolean) array1 ? "1," : "0,";
            } else {
                s += array1 + ",";
            }
        }
        s +=")";
        this.rep = s;
    }
    
    public T get(int i){
        return (T) this.array[i];
    }
    
    public int size(){
        return this.array.length;
    }
    
    @Override
    public final int hashCode(){
        return H;
    }
    
    @Override
    public boolean equals(Object o){
        if(this.hashCode()==o.hashCode()){
            if(o instanceof TupleImpl){
                for(int i = 0; i < this.array.length ; i++){
                    assert this.array[i] != null;
                    assert o != null;
                    if(!this.array[i].equals(((TupleImpl)o).get(i))){
                        //System.out.println("HERE "+this+" and "+((Tuple)o));
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString(){
        return rep;
    }
    
    @Override
    public T[] toArray(){
        return Arrays.copyOf(this.array,this.array.length);
    }
}
