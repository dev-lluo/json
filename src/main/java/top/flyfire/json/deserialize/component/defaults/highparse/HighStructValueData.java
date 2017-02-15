package top.flyfire.json.deserialize.component.defaults.highparse;

import top.flyfire.common.reflect.MetaInfo;
import top.flyfire.common.reflect.wrapper.InstanceWrapper;
import top.flyfire.json.deserialize.component.DStructSwap;

/**
 * Created by devll on 2016/11/1.
 */
public abstract class HighStructValueData<I,W extends InstanceWrapper,O> extends HighValueData<W,O> implements DStructSwap<I,MetaInfo> {

    protected I index;

    public HighStructValueData(W wrapper, O value, HighStructValueData owner) {
        super(wrapper,value, owner);
    }

    @Override
    public abstract MetaInfo indexing(I index);

    @Override
    public abstract void push(Object value);

    @Override
    public void destroy() {
        super.destroy();
        this.index = null;
    }

}
