package top.flyfire.json.deserialize.component.defaults.parse;

import java.util.List;

/**
 * Created by shyy_work on 2016/10/21.
 */
public class ArrayData extends StructValueData<Integer,List> {
    public ArrayData(List value, StructValueData owner) {
        super(value, owner);
    }

    @Override
    public StructValueData push(Object value) {
        this.value.add(this.index,value);
        return this;
    }
}
