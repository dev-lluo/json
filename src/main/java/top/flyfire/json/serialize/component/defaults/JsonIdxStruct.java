package top.flyfire.json.serialize.component.defaults;

import top.flyfire.json.serialize.component.Render;
import top.flyfire.json.serialize.component.Structed;


/**
 * Created by devll on 2016/11/23.
 */
public abstract class JsonIdxStruct<T> extends JsonRender<T> implements Structed {
    public JsonIdxStruct(T value, Render parent) {
        super(value, parent);
    }

    protected class TransferForIdx implements Transfer<Integer>{

        private Integer i;

        private Render v;

        public TransferForIdx(Integer i, Object object) {
            this.i = i;
            this.v = buildRender(object,JsonIdxStruct.this);
        }

        @Override
        public Integer indexing() {
            return i;
        }

        @Override
        public Render value() {
            return v;
        }
    }
}
