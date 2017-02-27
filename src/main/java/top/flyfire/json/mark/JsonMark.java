package top.flyfire.json.mark;

import top.flyfire.json.JsonRoute;

/**
 * Created by devll on 2017/2/17.
 */
public abstract class JsonMark {

    protected JsonRoute route;

    public JsonMark(JsonRoute route) {
        this.route = route;
    }

    public int getLevel() {
        return route.getLevel();
    }

    public String getPath() {
        return route.get();
    }

}
