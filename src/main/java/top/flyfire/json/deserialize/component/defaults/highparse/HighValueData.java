package top.flyfire.json.deserialize.component.defaults.highparse;

import top.flyfire.common.ObjectUtils;
import top.flyfire.common.reflect.wrapper.Wrapper;
import top.flyfire.json.JsonSwap;
import top.flyfire.json.deserialize.component.defaults.AbstractValueData;

/**
 * Created by devll on 2016/11/1.
 */
public abstract class HighValueData<W extends Wrapper,V> extends AbstractValueData<V,HighStructValueData> {

    protected W wrapper;

    public HighValueData(W wrapper, V cached, HighStructValueData highStructValueData) {
        super(cached, highStructValueData);
        this.wrapper = wrapper;
    }

    @Override
    public HighStructValueData getParent() {
        return super.getParent();
    }

    @Override
    public void destroy() {
        super.destroy();
        this.wrapper = null;
    }
}
