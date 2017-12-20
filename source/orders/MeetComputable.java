/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders;

/**
 * A class that is endowed with a meet operation.
 * @author qgbrabant
 */
public interface MeetComputable extends PartiallyOrderable{
    public Object meet(Object t);
}
