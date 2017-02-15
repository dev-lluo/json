package top.flyfire.json.deserialize.component;

/**
 * Created by shyy_work on 2016/10/21.
 */
public interface DStructSwap<I,R> {

    R indexing(I index);

    void push(Object value);

}
