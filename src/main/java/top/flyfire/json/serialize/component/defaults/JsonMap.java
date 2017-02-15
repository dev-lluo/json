package top.flyfire.json.serialize.component.defaults;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by devll on 2016/11/22.
 */
public class JsonMap extends JsonKyStruct<Map> {

    protected Iterator<Map.Entry> iterator;

    public JsonMap(Map value, JsonValue parent) {
        super(value, parent);
    }

    @Override
    public boolean notEmptyAndPeekStart() {
        iterator = this.cached.entrySet().iterator();
        return iterator.hasNext();
    }

    @Override
    public Transfer peeking() {
        Map.Entry entry = iterator.next();
        return new TransferForKy(String.valueOf(entry.getKey()),entry.getValue());
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }


}
