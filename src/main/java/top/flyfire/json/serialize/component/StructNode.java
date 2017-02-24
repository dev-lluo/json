package top.flyfire.json.serialize.component;

import top.flyfire.json.serialize.component.defaults.JsonValue;

/**
 * Created by devll on 2016/11/21.
 */
public interface StructNode {

    boolean notEmptyAndPeekStart();

    Entry peeking();

    boolean hasNext();

    interface Entry<I> {

        I indexing();

        JsonValue value();

    }

}
