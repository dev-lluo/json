package top.flyfire.json.deserialize.component.defaults.highparse;

import top.flyfire.common.reflect.wrapper.ValueWrapper;
import top.flyfire.common.reflect.wrapper.Wrapper;
import top.flyfire.json.deserialize.component.ValueComponent;

/**
 * Created by devll on 2016/11/1.
 */
public class HighValueData<W extends Wrapper,O> implements ValueComponent<Object> {

    protected W wrapper;

    protected HighStructValueData owner;

    protected O value;

    public HighValueData(W wrapper, O value, HighStructValueData owner) {
        this.value = value;
        this.owner = owner;
        this.wrapper = wrapper;
    }

    @Override
    public Object getValue() {
        try {
            return value;
        }finally {
            destroy();
        }
    }

    public Wrapper getWrapper(){
        return this.wrapper;
    }

    @Override
    public HighStructValueData render() {
        return this.owner==null?null:this.owner.push(getValue());
    }

    @Override
    public void destroy() {
        this.value = null;
        this.owner = null;
        this.wrapper = null;
    }
}
