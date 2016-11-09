package top.flyfire.json.component.defaults.parse;

import top.flyfire.json.component.ValueComponent;

/**
 * Created by shyy_work on 2016/10/21.
 */
public class ValueData<V> implements ValueComponent<V> {

    protected StructValueData owner;

    protected V value;

    public ValueData(V value,StructValueData owner){
        this.value = value;
        this.owner = owner;
    }

    @Override
    public V getValue() {
        try {
            return value;
        }finally {
            destroy();
        }
    }

    @Override
    public StructValueData render() {
        return this.owner==null?null:this.owner.push(getValue());
    }

    @Override
    public void destroy() {
        this.value = null;
        this.owner = null;
    }
}
