package top.flyfire.json.serialize.component.defaults;

import java.lang.reflect.Array;

/**
 * Created by devll on 2016/11/22.
 */
public class JsonArray extends JsonIdxStruct{

    private int length,cursor;

    public JsonArray(Object value, JsonValue parent) {
        super(value, parent);
        length = Array.getLength(value);
        cursor = -1;
    }

    @Override
    public boolean notEmptyAndPeekStart() {
        return ++cursor<length;
    }

    @Override
    public Transfer peeking() {
        return new TransferForIdx(cursor,Array.get(cached,cursor));
    }

    @Override
    public boolean hasNext() {
        return ++cursor<length;
    }
}
