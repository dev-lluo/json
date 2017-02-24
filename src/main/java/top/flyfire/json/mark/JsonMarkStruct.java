package top.flyfire.json.mark;

/**
 * Created by devll on 2017/2/17.
 */
public class JsonMarkStruct extends JsonMark {

    private boolean forObject;

    public JsonMarkStruct(int level, String path, boolean forObject) {
        super(level, path);
        this.forObject = forObject;
    }

    public boolean isForObject() {
        return forObject;
    }
}
