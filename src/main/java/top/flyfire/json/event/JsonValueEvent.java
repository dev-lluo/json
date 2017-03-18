package top.flyfire.json.event;


import top.flyfire.json.JsonRoute;

/**
 * Created by devll on 2017/2/17.
 */
public class JsonValueEvent extends JsonWorkEvent {

    private Object value;

    private boolean isNull;

    private boolean isUndefined;

    private boolean hasWrapper;

    public JsonValueEvent(JsonRoute route) {
        super(route);
    }

    public Object getValue() {
        return value;
    }

    public boolean isNull() {
        return isNull;
    }

    public boolean isUndefined() {
        return isUndefined;
    }

    public boolean hasWrapper() {
        return hasWrapper;
    }

    public void flush(Object value,boolean isNull,boolean isUndefined,boolean hasWrapper){
        this.value = value;
        this.isNull = isNull;
        this.isUndefined = isUndefined;
        this.hasWrapper = hasWrapper;
    }

}
