package top.flyfire.json.mark;


import top.flyfire.json.JsonRoute;

/**
 * Created by devll on 2017/2/17.
 */
public class JsonMarkStruct extends JsonMark {

    private boolean forObject;

    public JsonMarkStruct(JsonRoute route) {
        super(route);
    }

    public boolean isForObject() {
        return forObject;
    }

    protected void flush(boolean forObject){
        this.forObject = forObject;
    }


}
