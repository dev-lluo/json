package top.flyfire.json.mark;

/**
 * Created by devll on 2017/2/17.
 */
public abstract class JsonMark {

    private int level;

    private String path;

    public JsonMark(int level, String path) {
        this.level = level;
        this.path = path;
    }

    public int getLevel() {
        return level;
    }

    public String getPath() {
        return path;
    }
}
