package top.flyfire.json.serialize.component.defaults;

import top.flyfire.json.serialize.component.Render;
import top.flyfire.json.serialize.component.Structed;

/**
 * Created by devll on 2016/11/23.
 */
public abstract class JsonKyStruct<T> extends JsonRender<T> implements Structed {
    public JsonKyStruct(T value, Render parent) {
        super(value, parent);
    }

    protected class TransferForKy implements Transfer<String>{

        private String i;

        private Render v;

        public TransferForKy(String i,Object v) {
            this.i = i;
            this.v =  buildRender(v, JsonKyStruct.this);
        }

        @Override
        public String indexing() {
            return i;
        }

        @Override
        public Render value() {
            return v;
        }
    }

}
