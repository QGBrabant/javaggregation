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
public interface Order<T>{
    public Integer relation(T x, T y);
    
    /**
     * 
     * @return a random element from the order (uniform probability).
     */
    public T randomValue();
    
    public static class VanillaImpl<T extends PartiallyOrderable> implements Order<T> {

        @Override
        public T randomValue() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Integer relation(T x, T y) {
            return x.relation(y);
        }
        
    }
}
