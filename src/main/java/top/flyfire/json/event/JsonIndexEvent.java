package top.flyfire.json.event;

import top.flyfire.json.JsonRoute;

/**
 * Created by devll on 2017/2/17.
 */
public class JsonIndexEvent extends JsonWorkEvent {

    private Object index;

    private boolean forObject;

    public JsonIndexEvent(JsonRoute route) {
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
