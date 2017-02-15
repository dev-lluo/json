package top.flyfire.json.deserialize.component.defaults.highparse;

import top.flyfire.common.reflect.wrapper.ArrayWrapper;

import java.util.Collection;

/**
 * Created by devll on 2016/12/6.
 */
public class ArrayValueData extends BuildOutValueData<ArrayWrapper,Collection> {

    public ArrayValueData(ArrayWrapper wrapper, Collection value, HighStructValueData owner) {
        super(wrapper, value, owner);
    }

    @Override
    public void push(Object value) {
        this.cached.add(value);
    }

    @Override
    public Object getValue() {
        try {
            return wrapper.rawValue(cached);
        }finally {
            destroy();
        }
    }
}
