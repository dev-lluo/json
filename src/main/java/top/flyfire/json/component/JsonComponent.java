package top.flyfire.json.component;

/**
 * Created by devll on 2016/10/18.
 */
public interface JsonComponent {

    void openArray(int level);

    void closeArray(int level);

    void openObject(int level);

    void closeObject(int level);

    void indexing(Object index,int level);

    void value(String value, int level);

}
