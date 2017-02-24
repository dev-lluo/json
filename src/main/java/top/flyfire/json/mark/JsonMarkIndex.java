package top.flyfire.json.mark;

/**
 * Created by devll on 2017/2/17.
 */
public class JsonMarkIndex extends JsonMark {

    private Object index;

    private boolean forObject;

    public JsonMarkIndex(int level, String path, Object index, boolean forObject) {
        super(level, path);
        this.index = index;
        this.forObject = forObject;
    }

    public Object getIndex() {
        return index;
    }

    public boolean isForObject() {
        return forObject;
    }
}
