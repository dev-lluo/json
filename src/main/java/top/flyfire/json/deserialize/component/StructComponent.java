package top.flyfire.json.deserialize.component;

/**
 * Created by shyy_work on 2016/10/21.
 */
public interface StructComponent<I,R> {

    R indexing(I index);

    StructComponent push(Object value);

}
