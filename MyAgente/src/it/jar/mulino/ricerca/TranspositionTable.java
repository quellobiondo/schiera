package it.jar.mulino.ricerca;


//import org.apache.commons.collections4.map.LRUMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ziro on 24/05/17.
 */
public class TranspositionTable {
    public static final int MAX_SIZE = (int)Math.pow(2, 22);

    public enum EntryType {
        LOWER_BOUND, UPPER_BOUND, EXACT_SCORE;
    }

    public static final class Entry {
        public double score;
        public it.jar.mulino.model.Mossa move;
        public EntryType type;
        public byte depth;
    }

    private Map<Long, Entry> table = new HashMap<>();//new LRUMap<Long, Entry>(MAX_SIZE);
    private int lowerBoundHits = 0;
    private int upperBoundHits = 0;
    private int exactScoreHits = 0;
    private int missedHits = 0;

    public int getTableHits() {
        return this.lowerBoundHits + this.upperBoundHits + this.exactScoreHits;
    }
    public double getHitRatio() {
        int totalHits = this.getTableHits();
        if (totalHits == 0 && this.missedHits == 0) {
            return 0;
        } else {
            return ((double)totalHits / (double)(totalHits + this.missedHits));
        }
    }

    public Entry get(final Long hash, final byte depth) {
        Entry entry = this.table.get(hash);
        if (entry != null && entry.depth >= depth) {
            switch (entry.type) {
                case UPPER_BOUND:
                    this.upperBoundHits++;
                    break;
                case LOWER_BOUND:
                    this.lowerBoundHits++;
                    break;
                case EXACT_SCORE:
                    this.exactScoreHits++;
                    break;
            }

            return entry;
        } else {
            this.missedHits++;
        }

        return null;
    }

    public void put(final Long hash, final Entry entry) {
        Entry oldEntry = this.table.get(hash);
        if (oldEntry == null || oldEntry.depth <= entry.depth) {
            this.table.put(hash, entry);
        }
    }

    public void clear() {
        this.table.clear();
        this.upperBoundHits = 0;
        this.lowerBoundHits = 0;
        this.exactScoreHits = 0;
        this.missedHits = 0;
    }

    public int size() {
        return this.table.size();
    }
}
