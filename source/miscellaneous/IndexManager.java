/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscellaneous;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for generating indexes. Yes, this is overkill.
 * @author qgbrabant
 */
public class IndexManager {

    private final List<Counter> indexCounts;

    private IndexManager() {
        indexCounts = new ArrayList<>();
    }

    private static class IndexManagerHolder {
        private final static IndexManager INSTANCE = new IndexManager();
    }

    private static IndexManager getInstance() {
        return IndexManagerHolder.INSTANCE;
    }

    public static IndexGroupKey createIndexGroupKey() {
        IndexManager.getInstance().indexCounts.add(new Counter());
        return new IndexGroupKey(IndexManager.getInstance().indexCounts.size()-1);
    }

    private int nextIndex(int indexGroup) {
        this.indexCounts.get(indexGroup).i++;
        return this.indexCounts.get(indexGroup).i-1;
    }

    public static class IndexGroupKey {

        private final int id;

        private IndexGroupKey(int i) {
            this.id = i;
        }

        public int nextIndex() {
            return IndexManager.getInstance().nextIndex(this.id);
        }
    }

    private static class Counter {
        private int i = 0;
    }
}
