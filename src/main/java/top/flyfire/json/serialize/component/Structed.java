package top.flyfire.json.serialize.component;

/**
 * Created by devll on 2016/11/21.
 */
public interface Structed {

    boolean notEmptyAndPeekStart();

    Transfer peeking();

    boolean hasNext();

    interface Transfer<I> {

        I indexing();

        Render value();

    }

}
