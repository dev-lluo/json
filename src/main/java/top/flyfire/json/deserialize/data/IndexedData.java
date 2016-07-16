package top.flyfire.json.deserialize.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shyy_work on 2016/6/21.
 */
public class IndexedData extends Data<List,Integer> {
    @Override
    public void set(Object object) {
        this.value.add(extra,object);
    }

    @Override
    public void setExtra(Integer integer) {
        this.extra = integer;
    }


    public IndexedData() {
        super();
        this.value = new ArrayList();
    }

    public IndexedData(Data parent) {
        super(parent);
        this.value = new ArrayList();
    }
}
