package top.flyfire.json.component.defaults.highparse;

import top.flyfire.common.reflect.wrapper.Wrapper;
import top.flyfire.json.component.ValueComponent;
import top.flyfire.json.component.defaults.parse.StructValueData;

/**
 * Created by devll on 2016/11/1.
 */
public class HighValueData implements ValueComponent<Object> {

    protected Wrapper wrapper;

    protected HighStructValueData owner;

    protected Object value;

    public HighValueData(Wrapper wrapper, Object value, HighStructValueData owner) {
        this.value = value;
        this.owner = owner;
        this.wrapper = wrapper;
    }

    @Override
    public Object getValue() {
        try {
            return this.wrapper.rawValue(value);
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
    }
}
