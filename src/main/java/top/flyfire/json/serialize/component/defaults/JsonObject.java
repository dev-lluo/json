package top.flyfire.json.serialize.component.defaults;

import top.flyfire.json.serialize.component.Render;
import top.flyfire.json.serialize.component.Structed;

/**
 * Created by devll on 2016/11/22.
 */
public class JsonObject extends JsonKyStruct {

    public JsonObject(Object value, Render parent) {
        super(value, parent);
    }

    @Override
    public boolean notEmptyAndPeekStart() {
        return false;
    }

    @Override
    public Transfer peeking() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return false;
    }
}
