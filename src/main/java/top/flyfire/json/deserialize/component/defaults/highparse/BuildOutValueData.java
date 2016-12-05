package top.flyfire.json.deserialize.component.defaults.highparse;

import top.flyfire.common.reflect.MetaInfo;
import top.flyfire.common.reflect.wrapper.BuildOutWrapper;

/**
 * Created by devll on 2016/12/5.
 */
public class BuildOutValueData extends HighStructValueData<Object,BuildOutWrapper> {

    private MetaInfo metaInfo;

    public BuildOutValueData(BuildOutWrapper wrapper, Object value, HighStructValueData owner) {
        super(wrapper, value, owner);
        metaInfo = wrapper.getMetaInfo();
    }

    @Override
    public MetaInfo indexing(Object index) {
        this.index = index;
        return metaInfo;
    }

    @Override
    public HighStructValueData push(Object value) {
        this.wrapper.set(this.index, this.value, value);
        return this;
    }

    @Override
    public void destroy() {
        super.destroy();
        metaInfo = null;
    }
}
