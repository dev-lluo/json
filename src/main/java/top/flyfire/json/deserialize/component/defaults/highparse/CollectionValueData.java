package top.flyfire.json.deserialize.component.defaults.highparse;

import top.flyfire.common.reflect.wrapper.CollectionWrapper;

import java.util.Collection;

/**
 * Created by devll on 2016/12/6.
 */
public class CollectionValueData extends BuildOutValueData<CollectionWrapper,Collection> {

    public CollectionValueData(CollectionWrapper wrapper, Collection value, HighStructValueData owner) {
        super(wrapper, value, owner);
    }

    @Override
    public HighStructValueData push(Object value) {
        this.value.add(value);
        return this;
    }
}
