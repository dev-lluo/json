package top.flyfire.json;

import top.flyfire.common.Destroyable;

/**
 * Created by shyy_work on 2017/2/15.
 */
public abstract class JsonNode<Cache,Parent extends JsonNode> implements Destroyable {

    protected Cache cached;

    protected Parent parent;

    public JsonNode(Cache cached, Parent parent) {
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
