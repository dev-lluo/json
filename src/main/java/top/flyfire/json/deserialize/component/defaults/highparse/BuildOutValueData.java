package top.flyfire.json.deserialize.component.defaults.highparse;

import top.flyfire.common.reflect.MetaInfo;
import top.flyfire.common.reflect.wrapper.BuildOutWrapper;

/**
 * Created by devll on 2016/12/5.
 */
public abstract class BuildOutValueData<W extends BuildOutWrapper,O> extends HighStructValueData<Object,W,O> {

    private MetaInfo metaInfo;

    public BuildOutValueData(W wrapper, O value, HighStructValueData owner) {
        super(wrapper, value, owner);
        metaInfo = wrapper.getMetaInfo();
    }

    @Override
    public MetaInfo indexing(Object index) {
        this.index = index;
        return metaInfo;
    }

    @Override
    public abstract void push(Object value);

    @Override
    public void destroy() {
        super.destroy();
        metaInfo = null;
    }
}
