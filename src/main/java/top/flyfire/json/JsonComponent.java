package top.flyfire.json;

/**
 * Created by devll on 2016/10/18.
 */
public interface JsonComponent<T> {

    void openArray(int level);

    void closeArray(int level);

    void openObject(int level);

    void closeObject(int level);

    void indexing(Object index,int level);

    void value(Object value, int level);

    void toNext(int level);

    T result();

}
