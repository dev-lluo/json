package top.flyfire.json.component.defaults.parse;

import java.util.Map;

/**
 * Created by shyy_work on 2016/10/21.
 */

public class ObjectData extends StructValueData<String,Map> {

    public ObjectData(Map value, StructValueData owner) {
        super(value, owner);
    }

    @Override
    public StructValueData push(Object value) {
        this.value.put(this.index,value);
        return this;
    }

}
