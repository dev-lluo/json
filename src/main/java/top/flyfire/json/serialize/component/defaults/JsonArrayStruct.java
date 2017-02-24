package top.flyfire.json.serialize.component.defaults;



/**
 * Created by devll on 2016/11/23.
 */
public abstract class JsonArrayStruct<T> extends JsonStruct<T,Integer>{
    public JsonArrayStruct(T cached, JsonValue o) {
        super(cached, o);
    }
}
