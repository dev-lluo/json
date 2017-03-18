package top.flyfire.json.event;

import top.flyfire.json.JsonRoute;

/**
 * Created by devll on 2017/2/17.
 */
public abstract class JsonWorkEvent {

    protected JsonRoute route;

    public JsonWorkEvent(JsonRoute route) {
        this.route = route;
    }

    public int getLevel() {
        return route.getLevel();
    }

    public <T> T getCurrent() {
        return route.get();
    }

}
