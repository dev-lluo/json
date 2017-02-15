package top.flyfire.json.deserialize.component.defaults.parse;

import top.flyfire.common.ObjectUtils;
import top.flyfire.json.JsonSwap;
import top.flyfire.json.deserialize.component.defaults.AbstractValueData;

/**
 * Created by shyy_work on 2016/10/21.
 */
public class ValueData<V> extends AbstractValueData<V,StructValueData> {

    public ValueData(V cached, StructValueData structValueData) {
        super(cached, structValueData);
    }

    @Override
    public StructValueData getParent() {
        return super.getParent();
    }
}
