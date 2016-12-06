package top.flyfire.json.deserialize.component.defaults.highparse;

import top.flyfire.common.reflect.wrapper.ValueWrapper;

/**
 * Created by devll on 2016/12/6.
 */
public class HighRawValueData extends HighValueData<ValueWrapper,Object> {

    public HighRawValueData(ValueWrapper wrapper, Object value, HighStructValueData owner) {
        super(wrapper, value, owner);
    }

    @Override
    public Object getValue() {
        try {
            return wrapper.rawValue(value);
        }finally {
            destroy();
        }
    }
}
