package top.flyfire.json.deserialize;

/**
 * Created by devll on 2016/10/18.
 */
public interface DeserializeTrigger {

    /**
     * json newStruct
     * @param type  0:Array,1:Object
     */
    void newStruct(int type,int level);

    void endStruct(int type,int level);

    void indexing(Object index,int level);

    void rawValue(String value,int level);


}
