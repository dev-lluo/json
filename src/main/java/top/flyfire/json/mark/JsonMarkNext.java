package top.flyfire.json.mark;

/**
 * Created by devll on 2017/2/17.
 */
public class JsonMarkNext extends JsonMark {

    private boolean hasNext;

    public JsonMarkNext(int level, String path, boolean hasNext) {
        super(level, path);
        this.hasNext = hasNext;
    }

    public boolean hasNext() {
        return hasNext;
    }
}
