package top.flyfire.json.deserialize.component;

/**
 * Created by shyy_work on 2016/10/21.
 */
public interface ValueComponent<V> {

    V getValue();

    StructComponent render();

    void destroy();
}
