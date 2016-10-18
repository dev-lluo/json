package top.flyfire.json.component;

/**
 * Created by devll on 2016/10/18.
 */
public interface JsonComponent<T> {

    /**
     * json newStruct
     * @param type  0:Array,1:Object
     */
    void newStruct(int type,int level);

    void endStruct(int type,int level);

    void indexing(Object index,int level);

    void value(String value, int level);

    T storage();

}
