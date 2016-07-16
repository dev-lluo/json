package top.flyfire.json.deserialize.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shyy_work on 2016/6/21.
 */
public class StructedData extends Data<Map,String> {

    @Override
    public void setExtra(String s) {
        this.extra = s;
    }

    @Override
    public void set(Object object) {
        this.value.put(extra,object);
    }

    public StructedData() {
        super();
        this.value = new HashMap();
    }

    public StructedData(Data parent) {
        super(parent);
        this.value = new HashMap();
    }

}
