package top.flyfire.json.serialize.component.defaults;

import top.flyfire.json.serialize.component.StructSwap;


/**
 * Created by devll on 2016/11/23.
 */
public abstract class JsonIdxStruct<T> extends JsonValue<T> implements StructSwap {
    public JsonIdxStruct(T cached, JsonValue o) {
        super(cached, o);
    }

    protected class TransferForIdx implements Transfer<Integer>{

        private Integer i;

        private JsonValue v;

        public TransferForIdx(Integer i, Object object) {
            this.i = i;
            this.v = buildRender(object,JsonIdxStruct.this);
        }

        @Override
        public Integer indexing() {
            return i;
        }

        @Override
        public JsonValue value() {
            return v;
        }
    }
}
