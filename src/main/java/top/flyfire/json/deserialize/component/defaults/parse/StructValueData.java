package top.flyfire.json.deserialize.component.defaults.parse;

import top.flyfire.json.deserialize.component.StructComponent;

/**
 * Created by shyy_work on 2016/10/21.
 */
public abstract class StructValueData<I,V>  extends ValueData<V> implements StructComponent<I> {

    protected I index;

    public StructValueData(V value, StructValueData owner) {
        super(value, owner);
    }

    @Override
    public void indexing(I index) {
        this.index = index;
    }

    @Override
    public abstract StructValueData push(Object value);

    @Override
    public void destroy() {
        super.destroy();
        this.index = null;
    }
}
