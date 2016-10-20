package top.flyfire.json.component;

/**
 * Created by devll on 2016/10/20.
 */
public abstract class DataComponent<I,V> {

    protected DataComponent parent;

    protected V value;

    public DataComponent(V value){
        this.value = value;
    }

    public abstract void setIndexing(I indexing);

    public abstract DataComponent set(Object value);

    public DataComponent render(){
        return parent.set(value);
    }

    public void destroy(){
        this.parent = null;
        this.value = null;
    }

}
