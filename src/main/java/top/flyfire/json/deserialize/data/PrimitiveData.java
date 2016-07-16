package top.flyfire.json.deserialize.data;

/**
 * Created by shyy_work on 2016/6/21.
 */
public class PrimitiveData extends Data<Object,Class> {
    @Override
    public void set(Object object) {
        if(object==null)return;
        if(extra.isInstance(object)){
            this.value = object;
        }else{

        }
    }

    @Override
    public void setExtra(Class aClass) {
        this.extra = aClass;
    }

    public PrimitiveData() {
        super();
    }

    public PrimitiveData(Data parent) {
        super(parent);
    }
}
