package io.mobimenu.common.api;

/**
 * Simple class to represent pagination controls, clients using this class
 * create a simple page specifying the offset and limit (size) of the data
 * they wish to retrieve
 */
public record Page(int offset, int limit) {

    public Page(int offset, int limit) {
        var off = offset;
        if (off <= 0) {
            off = 1;
        }

        var lim = limit;
        if (lim <= 0) {
            lim = 20;
        }
        this.offset = off;
        this.limit = lim;
    }

    public static Page of(int offset, int limit) {
        return new Page(offset, limit);
    }

    public int getOffset() {
        return offset - 1;
    }

    public int getLimit() {
        return limit;
    }
}
