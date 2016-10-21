package top.flyfire.json.component;

/**
 * Created by shyy_work on 2016/10/21.
 */
public interface ValueComponent<V> {

    V getValue();

    StructComponent render();

    void destroy();
}
