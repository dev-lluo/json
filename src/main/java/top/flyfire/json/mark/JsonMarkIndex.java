package top.flyfire.json.mark;

import top.flyfire.json.JsonRoute;

/**
 * Created by devll on 2017/2/17.
 */
public class JsonMarkIndex extends JsonMark {

    private Object index;

    private boolean forObject;

    public JsonMarkIndex(JsonRoute route) {
        super(route);
    }

    public Object getIndex() {
        return index;
    }

    public boolean isForObject() {
        return forObject;
    }

    protected void flush(Object index,boolean forObject){
        this.index = index;
        this.forObject = forObject;
    }

}
