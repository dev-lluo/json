package top.flyfire.json.deserialize.component.defaults.highparse;

import top.flyfire.common.reflect.wrapper.MapWrapper;

import java.util.Map;

/**
 * Created by devll on 2016/12/6.
 */
public class MapValueData extends BuildOutValueData<MapWrapper,Map> {

    public MapValueData(MapWrapper wrapper, Map value, HighStructValueData owner) {
        super(wrapper, value, owner);
    }

    @Override
    public HighStructValueData push(Object value) {
        this.value.put(index,value);
        return this;
    }
}
