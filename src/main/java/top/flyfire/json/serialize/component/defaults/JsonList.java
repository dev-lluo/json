package top.flyfire.json.serialize.component.defaults;

import top.flyfire.json.serialize.component.Render;
import top.flyfire.json.serialize.component.Structed;

import java.util.Iterator;
import java.util.List;

/**
 * Created by devll on 2016/11/22.
 */
public class JsonList extends JsonIdxStruct<List> {

    protected int length,cursor;

    public JsonList(List value, Render parent) {
        super(value, parent);
        length = value.size();
        cursor = -1;
    }

    @Override
    public boolean notEmptyAndPeekStart() {
        return ++cursor<length;
    }

    @Override
    public Transfer peeking() {
        return new TransferForIdx(cursor,value.get(cursor));
    }

    @Override
    public boolean hasNext() {
        return ++cursor<length;
    }
}
