package top.flyfire.json.deserialize.component.defaults.parse;

import top.flyfire.json.deserialize.component.DStructSwap;

/**
 * Created by shyy_work on 2016/10/21.
 */
public abstract class StructValueData<I,V>  extends ValueData<V> implements DStructSwap<I,Void> {

    protected I index;

    public StructValueData(V value, StructValueData owner) {
        super(value, owner);
    }

    @Override
    public Void indexing(I index) {
        this.index = index;
        return null;
    }

    @Override
    public abstract void push(Object value);

    @Override
    public void destroy() {
        super.destroy();
        this.index = null;
    }
}
