package top.flyfire.json.event;

import top.flyfire.json.JsonRoute;

/**
 * Created by devll on 2017/2/27.
 */
public class JsonEventPool {

    private JsonStructEvent markStruct;

    private JsonIndexEvent markIndex;

    private JsonNextEvent markNext;

    private JsonValueEvent markValue;

    public JsonEventPool(JsonRoute route) {
        markStruct = new JsonStructEvent(route);
        markIndex = new JsonIndexEvent(route);
        markNext = new JsonNextEvent(route);
        markValue = new JsonValueEvent(route);
    }

    public JsonStructEvent borrowStruct(boolean forObject) {
        markStruct.flush(forObject);
        return markStruct;
    }

    public JsonIndexEvent borrowIndex(Object index, boolean forObject) {
        markIndex.flush(index,forObject);
        return markIndex;
    }

    public JsonNextEvent borrowNext(boolean hasNext) {
        markNext.flush(hasNext);
        return markNext;
    }

    public JsonValueEvent borrowValue(Object value, boolean isNull, boolean isUndefined, boolean hasWrapper) {
        markValue.flush(value,isNull,isUndefined,hasWrapper);
        return markValue;
    }

}
