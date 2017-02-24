package top.flyfire.json.serialize.component.defaults;

import top.flyfire.json.serialize.component.StructNode;

/**
 * Created by devll on 2017/2/15.
 */
public abstract class JsonStruct<T,I>  extends JsonValue<T> implements StructNode {

    public JsonStruct(T cached, JsonValue o) {
        super(cached, o);
    }

    protected class EntryImpl implements Entry<I> {

        private I i;

        private JsonValue v;

        public EntryImpl(I i, Object object) {
            this.i = i;
            this.v = buildValue(object,JsonStruct.this);
        }

        @Override
        public I indexing() {
            return i;
        }

        @Override
        public JsonValue value() {
            return v;
        }
    }
}
