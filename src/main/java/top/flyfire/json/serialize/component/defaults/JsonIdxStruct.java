package top.flyfire.json.serialize.component.defaults;



/**
 * Created by devll on 2016/11/23.
 */
public abstract class JsonIdxStruct<T> extends JsonStruct<T,Integer>{
    public JsonIdxStruct(T cached, JsonValue o) {
        super(cached, o);
    }
}
