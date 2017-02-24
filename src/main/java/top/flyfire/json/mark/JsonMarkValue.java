package top.flyfire.json.mark;

/**
 * Created by devll on 2017/2/17.
 */
public class JsonMarkValue extends JsonMark {

    private Object value;

    private boolean isNull;

    private boolean isUndefined;

    public JsonMarkValue(int level, String path, Object value, boolean isNull, boolean isUndefined) {
        super(level, path);
        this.value = value;
        this.isNull = isNull;
        this.isUndefined = isUndefined;
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
}
