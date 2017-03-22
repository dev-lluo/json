package top.flyfire.json.deserialize;

import java.util.Arrays;

/**
 * Created by shyy_work on 2017/3/22.
 */
public final class CharCached {

    private char[] value;

    private int count;

    public CharCached() {
        this(16);
    }

    public CharCached(int capacity){
        this.value = new char[capacity];
    }

    public int length() {
        return count;
    }

    public CharCached push(char c) {
        ensureCapacityInternal(count + 1);
        value[count++] = c;
        return this;
    }

    public void clear(){
        this.count = 0;
    }

    @Override
    public String toString() {
        return new String(value, 0, count);
    }

    private void ensureCapacityInternal(int minimumCapacity) {
        if (minimumCapacity - value.length > 0)
            expandCapacity(minimumCapacity);
    }

    void expandCapacity(int minimumCapacity) {
        int newCapacity = (value.length << 1) + 2;
        if (newCapacity - minimumCapacity < 0)
            newCapacity = minimumCapacity;
        if (newCapacity < 0) {
            if (minimumCapacity < 0) // overflow
                throw new OutOfMemoryError();
            newCapacity = Integer.MAX_VALUE;
        }
        value = Arrays.copyOf(value, newCapacity);
    }

}
