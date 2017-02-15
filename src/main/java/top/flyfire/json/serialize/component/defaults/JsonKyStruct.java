package top.flyfire.json.serialize.component.defaults;

import top.flyfire.json.serialize.component.StructSwap;

/**
 * Created by devll on 2016/11/23.
 */
public abstract class JsonKyStruct<T> extends JsonValue<T> implements StructSwap {
    public JsonKyStruct(T cached, JsonValue o) {
        super(cached, o);
    }

    protected class TransferForKy implements Transfer<String>{

        private String i;

        private JsonValue v;

        public TransferForKy(String i,Object v) {
            this.i = i;
            this.v =  buildRender(v, JsonKyStruct.this);
        }

        @Override
        public String indexing() {
            return i;
        }

        @Override
        public JsonValue value() {
            return v;
        }
    }

}
