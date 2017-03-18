package top.flyfire.json.event;

import top.flyfire.json.JsonRoute;

/**
 * Created by devll on 2017/2/17.
 */
public class JsonNextEvent extends JsonWorkEvent {

    private boolean hasNext;

    public JsonNextEvent(JsonRoute route) {
        super(route);
    }

    public boolean hasNext() {
        return hasNext;
    }

    protected void flush(boolean hasNext){
        this.hasNext = hasNext;
    }

}
