package top.flyfire.json;

import top.flyfire.common.Destroyable;

/**
 * Created by shyy_work on 2017/2/15.
 */
public abstract class JsonSwap<Cache,Parent extends JsonSwap> implements Destroyable {

    protected Cache cached;

    protected Parent parent;

    public JsonSwap(Cache cached, Parent parent) {
        this.cached = cached;
        this.parent = parent;
    }

    @Override
    public void destroy() {
        this.cached = null;
        this.parent = null;
    }

    public Cache getCached(){
        return cached;
    }

    public Parent getParent() {
        return parent;
    }
}
