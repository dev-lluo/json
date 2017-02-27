package top.flyfire.json.mark;


import top.flyfire.json.JsonRoute;

/**
 * Created by devll on 2017/2/17.
 */
public class JsonMarkValue extends JsonMark {

    private Object value;

    private boolean isNull;

    private boolean isUndefined;

    private boolean hasWrapper;

    public JsonMarkValue(JsonRoute route) {
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
