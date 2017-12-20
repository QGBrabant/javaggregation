/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders.impls;

import orders.MeetComputable;
import orders.OTools;
import orders.SemiLattice;

/**
 *
 * @author qgbrabant
 */
public class MeetableSet<T extends MeetComputable> extends MaxSet<T> implements MeetComputable{
    
    public MeetableSet(SemiLattice<T> p) {
        super(p);
    }
    
    public MeetableSet<T> meetersection(MeetableSet<T> other){
    MeetableSet<T> res = new MeetableSet<>((SemiLattice<T>)this.order);
        for(T w1 : this){
            for(T w2 : other){
                res.add(((SemiLattice<T>)this.order).meet(w1,w2));
            }
        }
        return res;
    }

    @Override
    public Object meet(Object t) {
        return this.meetersection((MeetableSet<T>) t);
    }

    @Override
    public Integer relation(Object o) {
        MeetableSet<T> ms = (MeetableSet<T>) o;

        Integer r = 0;
        Integer r2;
        boolean bigger;
        for(T w1 : this){
            bigger = true;
            for(T w2 : ms){
                r2 = this.order.relation(w1, w2);
                if(r2 != null){
                    if(r2 == 0){
                        bigger = false;
                    }else if(r2 > 0){
                        bigger = false;
                        r = OTools.cartesianProductOfRelations(r, 1);
                        /*System.out.println(w1);
                        System.out.println(w2);
                        System.out.println("1Change r:"+r);*/
                        break;
                    }else if (r2 == -1){
                        r = OTools.cartesianProductOfRelations(r, -1);
                        /*System.out.println(w1);
                        System.out.println(w2);
                        System.out.println("2Change r:"+r);*/
                        break;
                    }
                }
            }
            if(bigger){
                r = OTools.cartesianProductOfRelations(r, -1);
                //System.out.println(w1);
                //System.out.println("3Change r:"+r);
            }
            
            if(r == null || r==-1) break;
        }
        
        for(T w2 : ms){
            bigger = true;
            for(T w1 : this){
                r2 = this.order.relation(w2, w1);
                if(r2 != null){
                    if(r2 == 0){
                        bigger = false;
                    }else if(r2 > 0){
                        bigger = false;
                        r = OTools.cartesianProductOfRelations(r, -1);
                        /*System.out.println(w1);
                        System.out.println(w2);
                        System.out.println("4Change r:"+r);*/
                        break;
                    }else if (r2 == -1){
                        r = OTools.cartesianProductOfRelations(r, 1);
                        /*System.out.println(w1);
                        System.out.println(w2);
                        System.out.println("5Change r:"+r);*/
                        break;
                    }
                }
            }
            if(bigger){
                r = OTools.cartesianProductOfRelations(r, 1);
                        /*System.out.println(w2);
                        System.out.println("6Change r:"+r);*/
            }
            if(r == null || r == 1) break;
        }
        
        return r;
    }
}
