package top.flyfire.json.event;


import top.flyfire.json.JsonRoute;

/**
 * Created by devll on 2017/2/17.
 */
public class JsonStructEvent extends JsonWorkEvent {

    private boolean forObject;

    public JsonStructEvent(JsonRoute route) {
        super(route);
    }

    public boolean isForObject() {
        return forObject;
    }

    protected void flush(boolean forObject){
        this.forObject = forObject;
    }


}
