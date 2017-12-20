/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders;

/**
 * A class that is endowed with a join operation.
 * @author qgbrabant
 */
public interface JoinComputable extends PartiallyOrderable{
    public Object join(Object o);
}
