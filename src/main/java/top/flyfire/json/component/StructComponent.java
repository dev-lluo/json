package top.flyfire.json.component;

/**
 * Created by shyy_work on 2016/10/21.
 */
public interface StructComponent<I> {

    void indexing(I index);

    StructComponent push(Object value);

}
