package top.flyfire.json.serialize.component.defaults;

/**
 * Created by devll on 2016/11/23.
 */
public abstract class JsonObjectStruct<T> extends JsonStruct<T,String> {
    public JsonObjectStruct(T cached, JsonValue o) {
        super(cached, o);
    }
}
