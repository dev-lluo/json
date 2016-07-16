package top.flyfire.json.deserialize.data;

/**
 * Created by shyy_work on 2016/6/21.
 */
public abstract class Data<V,E>{

    protected V value;

    protected E extra;

    protected Data parent;

    public abstract void setExtra(E e);

    public abstract void set(Object object);

    public final V flush(){
        try {
            return value;
        }finally {
            destroy();
        }
    }

    public final Data flush2Parent(){
        try{
            parent.set(this.value);
            return parent;
        }finally {
            destroy();
        }
    }

    private void destroy(){
        this.parent = null;
        this.value = null;
        this.extra = null;
    }


    public Data() {
        super();
    }

    public Data(Data parent) {
        this.parent = parent;
    }
}
