/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscellaneous;

import java.util.HashMap;

/**
 * A HashMap with a default value that is returned instead of null when defaultGet(K) function is called.
 * @author qgbrabant
 */
public class DefaultHashMap<K, V> extends HashMap<K, V> {

    private final V defaultValue;

    public DefaultHashMap(V d) {
        super();
        this.defaultValue = d;
    }

    public V defaultGet(K t) {
        V res = this.get(t);
        return res != null ? res : this.defaultValue;
    }
}
