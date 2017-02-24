package top.flyfire.json.serialize.component.defaults;

import java.util.List;

/**
 * Created by devll on 2016/11/22.
 */
public class JsonList extends JsonArrayStruct<List> {

    protected int length,cursor;

    public JsonList(List value, JsonValue parent) {
        super(value, parent);
        length = value.size();
        cursor = -1;
    }

    @Override
    public boolean notEmptyAndPeekStart() {
        return ++cursor<length;
    }

    @Override
    public Entry peeking() {
        return new EntryImpl(cursor,cached.get(cursor));
    }

    @Override
    public boolean hasNext() {
        return ++cursor<length;
    }
}
