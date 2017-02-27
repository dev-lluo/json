package top.flyfire.json.mark;

import top.flyfire.json.JsonRoute;

/**
 * Created by devll on 2017/2/27.
 */
public class JsonMarkPool {

    private JsonMarkStruct markStruct;

    private JsonMarkIndex markIndex;

    private JsonMarkNext markNext;

    private JsonMarkValue markValue;

    public JsonMarkPool(JsonRoute route) {
        markStruct = new JsonMarkStruct(route);
        markIndex = new JsonMarkIndex(route);
        markNext = new JsonMarkNext(route);
        markValue = new JsonMarkValue(route);
    }

    public JsonMarkStruct borrowStruct(boolean forObject) {
        markStruct.flush(forObject);
        return markStruct;
    }

    public JsonMarkIndex borrowIndex(Object index,boolean forObject) {
        markIndex.flush(index,forObject);
        return markIndex;
    }

    public JsonMarkNext borrowNext(boolean hasNext) {
        markNext.flush(hasNext);
        return markNext;
    }

    public JsonMarkValue borrowValue(Object value,boolean isNull,boolean isUndefined,boolean hasWrapper) {
        markValue.flush(value,isNull,isUndefined,hasWrapper);
        return markValue;
    }

}
