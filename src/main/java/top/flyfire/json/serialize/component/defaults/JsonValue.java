package top.flyfire.json.serialize.component.defaults;

import top.flyfire.json.JsonSwap;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by devll on 2016/11/22.
 */
public class JsonValue<V> extends JsonSwap<V,JsonValue> {

    public JsonValue(V cached, JsonValue o) {
        super(cached, o);
    }

    @Override
    public JsonValue getParent() {
        return super.getParent();
    }

    public static JsonValue buildRender(Object value, JsonValue parent){
        if(value == null){
            return new JsonValue(value,parent);
        }else if(value instanceof String){
            return new JsonValue(value,parent);
        }else if(value.getClass().isPrimitive()){
            return new JsonValue(value,parent);
        }else if(value.getClass().isArray()){
            return new JsonArray(value,parent);
        }else if(value instanceof Date){
            return new JsonValue(value,parent);
        }else if(value instanceof Map){
            return new JsonMap((Map) value,parent);
        }else if(value instanceof List){
            return new JsonList((List) value,parent);
        }else if(value instanceof Set){
            return new JsonSet((Set) value,parent);
        }else{
            return new JsonObject(value,parent);
        }
    }
}
