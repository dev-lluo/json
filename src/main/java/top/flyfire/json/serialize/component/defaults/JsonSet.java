package top.flyfire.json.serialize.component.defaults;

import top.flyfire.json.serialize.component.Render;
import top.flyfire.json.serialize.component.Structed;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by devll on 2016/11/22.
 */
public class JsonSet extends JsonIdxStruct<Set>{

    protected Iterator iterator;

    protected int cursor;

    public JsonSet(Set value, Render parent) {
        super(value, parent);
    }

    @Override
    public boolean notEmptyAndPeekStart() {
        iterator = value.iterator();
        cursor++;
        return iterator.hasNext();
    }

    @Override
    public Transfer peeking() {
        return new TransferForIdx(cursor,iterator.next());
    }

    @Override
    public boolean hasNext() {
        cursor++;
        return iterator.hasNext();
    }
}
