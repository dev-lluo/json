package top.flyfire.json.deserialize.component.defaults;

import top.flyfire.common.ObjectUtils;
import top.flyfire.json.JsonNode;
import top.flyfire.json.deserialize.component.DStructSwap;

/**
 * Created by shyy_work on 2017/2/15.
 */
public class AbstractValueData<V,P extends JsonNode & DStructSwap> extends JsonNode<V,P> {

    public AbstractValueData(V cached, P p) {
        super(cached, p);
    }

    public Object getValue() {
        try {
            return getCached();
        }finally {
            destroy();
        }
    }

    @Override
    public P getParent() {
        try {
            return super.getParent();
        }finally {
            if(ObjectUtils.isNotNull(this.parent)){
                this.parent.push(getValue());
            }
        }
    }
}
