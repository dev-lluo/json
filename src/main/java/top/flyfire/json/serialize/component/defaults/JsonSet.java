package top.flyfire.json.serialize.component.defaults;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by devll on 2016/11/22.
 */
public class JsonSet extends JsonIdxStruct<Set>{

    protected Iterator iterator;

    protected int cursor;

    public JsonSet(Set value, JsonValue parent) {
        super(value, parent);
    }

    @Override
    public boolean notEmptyAndPeekStart() {
        iterator = cached.iterator();
        cursor++;
        return iterator.hasNext();
    }

    @Override
    public Entry peeking() {
        return new EntryImpl(cursor,iterator.next());
    }

    @Override
    public boolean hasNext() {
        cursor++;
        return iterator.hasNext();
    }
}
