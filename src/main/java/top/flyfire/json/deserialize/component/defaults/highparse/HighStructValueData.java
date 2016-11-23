package top.flyfire.json.deserialize.component.defaults.highparse;

import top.flyfire.common.reflect.wrapper.Wrapper;
import top.flyfire.json.deserialize.component.StructComponent;

/**
 * Created by devll on 2016/11/1.
 */
public class HighStructValueData<I,V> extends HighValueData implements StructComponent<I> {

    protected I index;

    public HighStructValueData(Wrapper wrapper, Object value, HighStructValueData owner) {
        super(wrapper,value, owner);
    }

    @Override
    public void indexing(I index) {
        this.index = index;
    }

    @Override
    public HighStructValueData push(Object value) {
       this.wrapper.set(this.index, this.value, value);
        return this;
    }

    @Override
    public void destroy() {
        super.destroy();
        this.index = null;
    }
}
