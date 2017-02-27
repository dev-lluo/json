package top.flyfire.json.mark;

import top.flyfire.json.JsonRoute;

/**
 * Created by devll on 2017/2/17.
 */
public class JsonMarkNext extends JsonMark {

    private boolean hasNext;

    public JsonMarkNext(JsonRoute route) {
        super(route);
    }

    public boolean hasNext() {
        return hasNext;
    }

    protected void flush(boolean hasNext){
        this.hasNext = hasNext;
    }

}
