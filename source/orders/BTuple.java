/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders;

import miscellaneous.Tuple;

/**
 * A tuple that contains boolean values.
 * @author qgbrabant
 */
public interface BTuple extends Tuple<Boolean>, MeetComputable, JoinComputable{
    public int getNumberOfOnes();
    public int getNumberOfZeros();
}
