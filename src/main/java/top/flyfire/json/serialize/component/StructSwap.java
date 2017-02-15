package top.flyfire.json.serialize.component;

import top.flyfire.json.serialize.component.defaults.JsonValue;

/**
 * Created by devll on 2016/11/21.
 */
public interface StructSwap {

    boolean notEmptyAndPeekStart();

    Transfer peeking();

    boolean hasNext();

    interface Transfer<I> {

        I indexing();

        JsonValue value();

    }

}
