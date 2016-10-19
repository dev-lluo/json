package top.flyfire.json.component;

/**
 * Created by devll on 2016/10/18.
 */
public interface JsonComponent {

    void openStruct(int type, int level);

    void closeStruct(int type, int level);

    void indexing(Object index,int level);

    void value(String value, int level);

}
